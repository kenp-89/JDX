package filesystem3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * <p>The <code>FileViewerWindow</code> class creates a file
 * manager window. It is an extension of the <code>JFrame</code> class.</p>
 * 
 * @author Ken Peck
 * @date   November 24th, 2014
 * 
 * <p>Copyright (c) 2014 Ken Peck.</p>
 */
@SuppressWarnings("serial")
public class FileViewerWindow extends JFrame implements ActionListener {
	/*
	 * This FileManager object will do all the heavy lifting for file operations,
	 * and will give most of the functionality to our code. 
	 */
	private FileManager fm;
	
	/*
	 * Our primary JPanels.
	 * goPanel: Contains the location bar
	 * btnPanel: Houses all of the buttons at the bottom of the window
	 * btninner: Is contained within btnPanel; Houses more buttons for
	 *           organizational purposes
	 */
	// TODO: add another JPanel for a menu bar.
	// TODO: make a better layout
	private JPanel goPanel;
	private JPanel btnPanel;
	private JPanel btninner;
	private JPanel btninnerwest;
	private JPanel btninnercent;
	private JPanel btninnereast;

	/*
	 * fl: our JList container for listing files in a directory
	 * sp: makes fl scrollable
	 */
	private JList<File> fl;
	private JScrollPane sp;
	
	// this is the location bar at the top of the window
	private JTextField curloc;

	// our various buttons
	// TODO: add a "Rename" button to edit filenames.
	private JButton homebtn;
	private JButton pdirbtn;
	private JButton openbtn;
	private JButton copybtn;
	private JButton pastebtn;
	private JButton delbtn;
	private JButton newdirbtn;
	
	// checkbox for showing hidden files
	private JCheckBox hideChk;
	
	// flag for determination of showing/hiding the 'hidden' files
	private boolean hidehid;

	// the internal representation of our file list
	private Vector<File> files;
	
	/**
	 * Main constructor. Sets up all the window elements and obtains initial
	 * fs data from the operating system.
	 */
	public FileViewerWindow(String title) {
		super(title);
		
		fm = new FileManager();

		// These panels are used for layout of the window.
		goPanel = new JPanel(new BorderLayout());
		btnPanel = new JPanel(new BorderLayout());
		btninner = new JPanel(new BorderLayout());
		btninnerwest = new JPanel(new BorderLayout());
		btninnercent = new JPanel(new BorderLayout());
		btninnereast = new JPanel(new BorderLayout());
		
		/*
		 * fl, or file list, which will display the contents of the current directory,
		 * along with sp, the scrollpane which contains it.
		 */
		fl = new JList<File>();
		sp = new JScrollPane(fl);
		
		// Our buttons and checkboxes
		homebtn = new JButton("Home");
		pdirbtn = new JButton("Up One Level");
		openbtn = new JButton("Open");
		copybtn = new JButton("Copy");
		pastebtn = new JButton("Paste");
		delbtn  = new JButton("Delete");
		newdirbtn = new JButton("New Folder");
		hideChk = new JCheckBox("Show Hidden Files");
		
		// initialize the location bar with our directory.
		curloc = new JTextField(fm.getDir().getPath());
		
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
//		pastebtn.setEnabled(false);
		
		/*
		 * we must first call refreshView() in order to display a directory listing
		 * in the JList. Afterwards, this should only be called in the
		 * actionPerformed() method. 
		 */
		refreshView();
	}
	
	// accessors for testing.
	public FileManager getFileManager() {
		return fm;
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
	
	public boolean getHidehid() {
		return hidehid;
	}
	
	public Vector<File> getFiles() {
		return files;
	}
	// end accessors
	
	/**
	 * <p>Transforms an array of <code>File</code> objects into a vector
	 * of <code>File</code> objects. This is done to make it easier to add additional
	 * members to the list in the future.</p>
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
	 * <p>Refreshes the list in the main window with the location
	 * referenced by the value of <code>dir</code>.</p>
	 */
	public void refreshView() {
		curloc.setText(fm.getDir().getPath());
		files = fileArToFileVec(fm.getView().getFiles(fm.getDir(), hidehid));
		// populate the list
		fl.setListData(files);
		/*
		 *  check clipboard status on each window update, and enable the
		 *  paste button if and only if the clipboard is non-empty.
		 */
		pastebtn.setEnabled(!fm.isClipBoardEmpty());
		
//		delbtn.setEnabled(!fl.isSelectionEmpty());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/* 
		 * copyFlag is only set to false when NO_OPTION is chosen in the option pane.
		 * We do this because if the pasteFile does not already exist on disk,
		 * we should copy anyway.
		 */
		boolean copyFlag = true;
		File f = null;
		// user presses enter while curloc is in focus.
		if (e.getSource().equals(curloc)) {
			try {
				fm.openFile(new File(curloc.getText()));
			} catch (IOException ioe) {
				System.err.println("Error opening file.");
			}
		}
		// user clicks the 'Home' button.
		else if (e.getSource().equals(homebtn)) {
			try {
				fm.openFile(fm.getView().getHomeDirectory());
			} catch (IOException ioe) {
				System.err.println("Error opening file.");
			}
		}
		// user clicks the 'Up One Level' button.
		else if (e.getSource().equals(pdirbtn)) {
			if (!fm.getView().isFileSystemRoot(fm.getDir())) {
				try {
					fm.openFile(fm.getView().getParentDirectory(fm.getDir()));
				} catch (IOException ioe) {
					System.err.println("Error opening file.");
				}
			}
			else
				System.out.println("This is the root directory.");
		}
		// user clicks the 'Open' button.
		else if (e.getSource().equals(openbtn)) {
			if (!fl.isSelectionEmpty()) {
				try {
					fm.openFile(fl.getSelectedValue());
				} catch (IOException ioe) {
					System.err.println("Error opening file.");
				}
			}
		}
		// user clicks the 'Copy' button.
		else if (e.getSource().equals(copybtn)) {
			if (!fl.isSelectionEmpty()) {
				if ((f = fm.copyToClip(fl.getSelectedValue())) != null)
					System.out.println("Successfully copied " + f.getPath() +
							" to the clip-board.");
				else {
					System.err.println("Could not copy " + fl.getSelectedValue());
				}
			}
		}
		// user clicks the 'Paste' button.
		else if (e.getSource().equals(pastebtn)) {
			if (new File(fm.getDir().getPath() +
					File.separator + fm.getClipFile().getName()).exists()) {
				// I hate lines that are this long. HATE THEM! -Ken
				if ((JOptionPane.showConfirmDialog(this,
						"The file " + fm.getDir().getPath() + File.separator +
						fm.getClipFile().getName() + 
						" already exists. Overwrite?",
						"File already exists.",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE))
						== JOptionPane.NO_OPTION)
					copyFlag = false;
			}
			if (copyFlag) {
				try {
					if (!fl.isSelectionEmpty()) {
//						fm.pasteFile(fm.getClipFile(), fl.getSelectedValue(),
//								fm.getDir());
						fm.copyFile(fm.getClipFile(),
								new File(fm.getDir() + File.separator +
										fl.getSelectedValue().getName()), false);
					}
					else {
//						fm.pasteFile(fm.getClipFile(), fm.getDir(),
//								fm.getDir().getParentFile());
						fm.copyFile(fm.getClipFile(),
								new File(fm.getDir().getPath() + File.separator +
										fm.getClipFile().getName()), false);
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
					System.err.println(ioe.getMessage());
				}
			}
		}
		// user clicks the 'Delete' button.
		else if (e.getSource().equals(delbtn)) {
			if (!fl.isSelectionEmpty()) {
				if (fl.getSelectedValue().exists()) {
					// display confirmation dialog
					int result = JOptionPane.showConfirmDialog(this,
							"Really delete " + fl.getSelectedValue().getPath() + "?" +
							"\n" + "(Seriously, you WILL lose this file PERMANENTLY!)",
							"Confirm deletion", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					// if user clicks the 'Yes' button, delete the file.
					if (result == JOptionPane.YES_OPTION)
						fm.deleteFile(fl.getSelectedValue());
				}
			}
		}
		// user clicks the 'New Folder' button.
		else if (e.getSource().equals(newdirbtn)) {
			fm.mkdir(fm.getDir());
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
