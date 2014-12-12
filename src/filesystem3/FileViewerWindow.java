package filesystem3;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

/**
 * <p>FileViewerWindow</p>
 * <p>Description: CS343 Group Project</p>
 * @author Ken Peck <ken.peck@my.uwrf.edu>
 * @author Jason Skinner <jason.skinner@my.uwrf.edu>
 * @author Robbie Fox <robert.fox@my.uwrf.edu>
 * @date   November 24th, 2014
 * 
 * <p>The <code>FileViewerWindow</code> class creates a file
 * manager window. It is an extension of the <code>JFrame</code> class.</p>
 * 
 * <p>This specific file was written by Ken Peck.</p>
 */
@SuppressWarnings("serial")
public class FileViewerWindow extends JFrame implements ActionListener {
	private static final String version = "5.1.3c";
	
	/*
	 * Our primary JPanels.
	 * goPanel: Contains the location bar
	 * btnPanel: Houses all of the buttons at the bottom of the window
	 * btninner: Is contained within btnPanel; Houses more buttons for
	 *           organizational purposes
	 */
	private JPanel goPanel;
	private JPanel btnPanel;
	private JPanel btninner;
	private JPanel btninnerwest;
	private JPanel btninnercent;
	private JPanel btninnereast;
	
	// view will get all filesystem information for using directories.
	private FileSystemView view;

	/*
	 * fl: our JList container for listing files in a directory
	 * sp: makes fl scrollable
	 */
	private JList<File> fl;
	private JScrollPane sp;
	
	// this is the location bar at the top of the window
	private JTextField curloc;
	
	// stat text - cannot use yet.
	private JLabel statText;

	// our various buttons
	private JButton homebtn;
	private JButton pdirbtn;
	private JButton openbtn;
	private JButton copybtn;
	private JButton pastebtn;
	private JButton delbtn;
	private JButton newdirbtn;
	
	// checkbox for showing hidden files
	private JCheckBox hideChk;
	
	// used to track the current directory
	private String dir;
	
	// flag for determination of showing/hiding the 'hidden' files
	private boolean hidehid;

	// the internal representation of our file list
	private Vector<File> files;
	
	// represents a file to be copied using pasteFile()
	private File clipFile;
	
	/**
	 * Main constructor. Sets up all the window elements and obtains initial
	 * fs data from the operating system.
	 */
	public FileViewerWindow() {
		super("Java File Manager v" + version);

		// These panels are used for layout of the window.
		goPanel = new JPanel(new BorderLayout());
		btnPanel = new JPanel(new BorderLayout());
		btninner = new JPanel(new BorderLayout());
		btninnerwest = new JPanel(new BorderLayout());
		btninnercent = new JPanel(new BorderLayout());
		btninnereast = new JPanel(new BorderLayout());
		
		// Here we get our fs data.
		view = FileSystemView.getFileSystemView();
		
		/*
		 * fl, or file list, which will display the contents of the current directory,
		 * along with sp, the scrollpane which contains it.
		 */
		fl = new JList<File>();
		sp = new JScrollPane(fl);
		
		statText = new JLabel("");
		
		// Our buttons and checkboxes
		homebtn = new JButton("Home");
		pdirbtn = new JButton("Up One Level");
		openbtn = new JButton("Open");
		copybtn = new JButton("Copy");
		pastebtn = new JButton("Paste");
		delbtn  = new JButton("Delete");
		newdirbtn = new JButton("New Folder");
		hideChk = new JCheckBox("Show Hidden Files");
		
		/*
		 * initialize our directory variable. This will be the starting
		 * location when the window becomes visible.
		 */
		dir = view.getHomeDirectory().getPath();
		
		// initialize the location bar with our directory.
		curloc = new JTextField(dir);
		
		/*
		 * default value for our hidden file flag.
		 * true:  hide the 'hidden' files.
		 * false: show the 'hidden' files.
		 */
		hidehid = true;
		
		/*
		 * set up several aspects of the window, such as
		 * layout, action listeners, etc.
		 */
		// layout setup: primary window
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(goPanel, BorderLayout.NORTH);
		this.add(sp, BorderLayout.CENTER);
		this.add(btnPanel, BorderLayout.SOUTH);
		this.add(new JPanel(), BorderLayout.WEST);
		this.add(new JPanel(), BorderLayout.EAST);
		
		// layout setup: top region
		goPanel.add(new JPanel(), BorderLayout.WEST);
		goPanel.add(curloc, BorderLayout.CENTER);
		goPanel.add(new JPanel(), BorderLayout.EAST);
		
		// layout setup: bottom region
		btnPanel.add(hideChk, BorderLayout.SOUTH);
		btnPanel.add(homebtn, BorderLayout.WEST);
		btnPanel.add(newdirbtn, BorderLayout.EAST);
		btnPanel.add(btninner, BorderLayout.CENTER);
		btninner.add(btninnerwest, BorderLayout.WEST);
		btninner.add(btninnercent, BorderLayout.CENTER);
		btninner.add(btninnereast, BorderLayout.EAST);
		btninnerwest.add(openbtn, BorderLayout.WEST);
		btninnercent.add(pdirbtn, BorderLayout.WEST);
		btninnercent.add(copybtn, BorderLayout.CENTER);
		btninnercent.add(pastebtn, BorderLayout.EAST);
		btninnereast.add(delbtn, BorderLayout.EAST);
		
		// setup all event handlers
		curloc.addActionListener(this);
		hideChk.addActionListener(this);
		homebtn.addActionListener(this);
		pdirbtn.addActionListener(this);
		openbtn.addActionListener(this);
		copybtn.addActionListener(this);
		pastebtn.addActionListener(this);
		delbtn.addActionListener(this);
		newdirbtn.addActionListener(this);
		
		/*
		 *  disable Paste button by default; will enable once a file
		 *  has been copied to clip-board.
		 */
		pastebtn.setEnabled(false);
		
		/*
		 * we must first call refreshView() in order to display a directory listing
		 * in the JList. Afterwards, this should only be called in the
		 * actionPerformed() method. 
		 */
		refreshView();
	}
	
	// accessors for testing. These really clutter up the code. I don't like them.
	public FileSystemView getView() {
		return view;
	}
	
	public JList<File> getFl() {
		return fl;
	}
	
	public JTextField getCurloc() {
		return curloc;
	}
	
	public JButton getHomebtn() {
		return homebtn;
	}
	
	public JButton getPdirbtn() {
		return pdirbtn;
	}
	
	public JButton getOpenbtn() {
		return openbtn;
	}
	
	public JButton getCopybtn() {
		return copybtn;
	}
	
	public JButton getPastebtn() {
		return pastebtn;
	}
	
	public JButton getDelbtn() {
		return delbtn;
	}
	
	public JButton getNewdirbtn() {
		return newdirbtn;
	}
	
	public JCheckBox getHideChk() {
		return hideChk;
	}
	
	public String getDir() {
		return dir;
	}
	
	public boolean getHidehid() {
		return hidehid;
	}
	
	public Vector<File> getFiles() {
		return files;
	}
	
	public File getClipFile() {
		return clipFile;
	}
	// end accessors. ugh
	
	// mutator for testing
	public void setClipFile(File cf) {
		clipFile = cf;
	}
	// end mutators
	
	/**
	 * <p>Transforms an array of <code>File</code> objects into a vector
	 * of <code>File</code> objects. This is necessary to comply with project
	 * requirements (use of Collections).</p>
	 * @param a
	 * @return A vector of <code>File</code> objects.
	 */
	private Vector<File> fileArToFileVec(File[] a) {
		Vector<File> v = new Vector<File>(a.length);
		// sequentially add members of array a[] to vector v
		for (int i = 0; i < a.length; i++) {
			v.add(a[i]);
		}
		return v;
	}
	
	/**
	 * <p>Copy a <code>File</code> object to the internal clip-board.
	 * The file can be pasted using <code>pasteFile</code>.</p>
	 * @param f
	 */
	private void copyFile(File f) {
		clipFile = new File(f.getPath());
		System.out.println("Successfully copied " + f.getPath() +
				" to the clip-board.");
		pastebtn.setEnabled(true);
	}

	/**
	 * <p>Pastes a the clip-board file to the location named by <code>f</code>.</p>
	 * @param f
	 */
	private void pasteFile(File f) {
		/* 
		 * copyFlag is only set to false when result == NO_OPTION.
		 * We do this because if the pasteFile does not already exist on disk,
		 * we should copy anyway.
		 */
		boolean copyFlag = true;
		// reference to the file to be pasted (along with its full path)
		File pasteFile;
		// used in copying clipFile to pasteFile
		FileChannel src = null;
		FileChannel dst = null;

		if (f.isDirectory()) {
			pasteFile = new File(f.getPath() + File.separator + clipFile.getName());
		}
		else {
			pasteFile = new File(dir + File.separator + clipFile.getName());
		}

		if (pasteFile.exists()) {
			// I hate lines that are this long. HATE THEM! -Ken
			if ((JOptionPane.showConfirmDialog(this,
					"The file " + pasteFile.getPath() + 
					" already exists. Overwrite?",
					"File already exists.",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE))
					== JOptionPane.NO_OPTION)
				copyFlag = false;
		}
		if (copyFlag) {
			try {
				pasteFile.createNewFile();
				src = new FileInputStream(clipFile).getChannel();
				dst = new FileOutputStream(pasteFile).getChannel();
				dst.transferFrom(src, 0, src.size());

				/*
				 * clean up - now that the file has been pasted, we nullify clipFile
				 * and disable the Paste button.
				 */
				clipFile = null;
				pastebtn.setEnabled(false);
			} catch (IOException e) {
				System.err.println("Couldn't paste file.");
			}
		}
	}
	
	/**
	 * <p>Deletes the <code>File</code> object named by selecting the appropriate
	 * term in the main <code>JList</code> object. It always asks for confirmation
	 * via a <code>JOptionPane</code> before deleting.</p>
	 * @param f
	 */
	private void deleteFile(File f) {
		if (f.exists()) {
			// display confirmation dialog
			int result = JOptionPane.showConfirmDialog(this,
					"Really delete " + f.getPath() + "?" + "\n" +
					"(Seriously, you WILL lose this file PERMANENTLY!)",
					"Confirm deletion", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			// if user clicks the 'Yes' button, delete the file.
			if (result == JOptionPane.YES_OPTION)
				f.delete();
		}
	}
	
	/**
	 * <p>Creates a new directory within the current directory.</p>
	 */
	private void mkdir() {
		if (!(new File(dir + File.separator + "Untitled").mkdir()))
			System.err.println("Could not create directory.");
	}

	/**
	 * <p>Opens the file specified by the <code>File</code> object <code>f</code>.
	 * If <code>f</code> is a directory, move to that directory. Otherwise, try to
	 * open through the OS.</p>
	 * @param f
	 */
	private void openFile(File f) {
		if (f.isDirectory()) {
				dir = f.getPath();
				System.out.println("Moving to " + f.getPath());
		}
		else {
			System.out.println("Opening " + f.getPath());
			try {
				Desktop.getDesktop().open(f);
			} catch (IOException e1) {
				System.err.println("Cannot open " + f.getPath());
			}
		}
	}
	
	/**
	 * <p>Refreshes the list in the main window with the location
	 * referenced by the value of <code>dir</code>.</p>
	 */
	public void refreshView() {
		curloc.setText(dir);
		files = fileArToFileVec(view.getFiles(new File(dir), hidehid));
		// populate the list
		fl.setListData(files);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// user presses enter while curloc is in focus.
		if (e.getSource().equals(curloc)) {
			openFile(new File(curloc.getText()));
		}
		// user clicks the 'Home' button.
		else if (e.getSource().equals(homebtn)) {
			openFile(view.getHomeDirectory());
		}
		// user clicks the 'Up One Level' button.
		else if (e.getSource().equals(pdirbtn)) {
			if (!view.isFileSystemRoot(new File(dir))) {
				openFile(view.getParentDirectory(new File(dir)));
			}
			else
				System.out.println("This is the root directory.");
		}
		// user clicks the 'Open' button.
		else if (e.getSource().equals(openbtn)) {
			if (!fl.isSelectionEmpty()) {
				openFile(fl.getSelectedValue());
			}
		}
		// user clicks the 'Copy' button.
		else if (e.getSource().equals(copybtn)) {
			if (!fl.isSelectionEmpty()) {
				copyFile(fl.getSelectedValue());
			}
		}
		// user clicks the 'Paste' button.
		else if (e.getSource().equals(pastebtn)) {
			if (!fl.isSelectionEmpty()) {
				pasteFile(fl.getSelectedValue());
			}
			else {
				pasteFile(new File(dir));
			}
		}
		// user clicks the 'Delete' button.
		else if (e.getSource().equals(delbtn)) {
			if (!fl.isSelectionEmpty()) {
					deleteFile(fl.getSelectedValue());
			}
		}
		// user clicks the 'New Folder' button.
		else if (e.getSource().equals(newdirbtn)) {
			mkdir();
		}
		// user clicks the 'Show Hidden Files' checkbox.
		else if (e.getSource().equals(hideChk)) {
			if (hideChk.isSelected()) {
				hidehid = false;
				System.out.println("Showing hidden files");
			}
			else {
				hidehid = true;
				System.out.println("Hiding hidden files");
			}
		}
		// some unidentified event occurred.
		else {
			System.out.println("HoochiMaMa!" + '\n'
					+ "Don't get peeved, but I really don't know what just happened.");
		}

		// done processing; refresh the window.
		refreshView();
	}
}
