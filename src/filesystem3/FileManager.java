package filesystem3;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.swing.filechooser.FileSystemView;

/**
 * <p>Represents a file manager.</p>
 * <p>Objects of this class can read and modify any mounted filesystems on a given
 * machine. Included is functionality to open, copy, and delete files and directories.
 * </p>
 * 
 * @author Ken Peck
 * @date   December 12th, 2014
 *
 * <p>Copyright (c) 2014 Ken Peck.</p>
 */
public class FileManager {
	
	private FileSystemView view;
	private File dir;
	private File clipFile;

	/**
	 * <p>Instantiates an object of class <code>FileManager</code>. Gets the
	 * default view of the filesystem, initializes the <code>FileManager</code>'s
	 * working directory to the user's home directory, and nullifies the clipboard.
	 * </p> 
	 */
	public FileManager() {
		updateView();
		dir = view.getHomeDirectory();
		clipFile = null;
	}
	
	/**
	 * <p>Returns the <code>File</code> stored in this object's clip-board.</p>
	 * @return A <code>File</code> object stored in the clip-board.
	 */
	public File getClipFile() {
		return clipFile;
	}
	
	/**
	 * <p>Returns the current working directory.</p>
	 * @return A <code>File</code> object representing the current directory.
	 */
	public File getDir() {
		return dir;
	}
	
	/**
	 * <p>Returns this object's current view of the filesystem.</p>
	 * @return A <code>FileSystemView</code> object representing the current view
	 * of the filesystem.
	 */
	public FileSystemView getView() {
		return view;
	}
	
	/**
	 * <p>Sets the clipboard to point to a specific file, if it exists. This method
	 * is functionally equivalent to <code>copyFile(File)</code>, save for the fact
	 * that it does not return anything. As such, for actual file copying, it is
	 * recommended that <code>copyFile</code> is used instead.</p>
	 * @param clipFile
	 */
	public void setClipFile(File clipFile) {
		if (clipFile.exists())
			this.clipFile = clipFile;
	}
	
	public void setDir(File dir) {
		if (dir.isDirectory())
			this.dir = dir;
	}
	
	/**
	 * <p>Sets the view of the filesystem to user specification.</p>
	 * @param view
	 */
	public void setView(FileSystemView view) {
		this.view = view;
	}
	
	/**
	 * <p>Gets the status of the internal clipboard.  If empty, return true. Otherwise,
	 * return false.</p>
	 * @return A boolean value representing clipboard status.
	 */
	public boolean isClipBoardEmpty() {
		if (clipFile == null)
			return true;
		else 
			return false;
	}
	
	/**
	 * <p>Resets the view of the filesystem to the default.</p>
	 */
	public void updateView() {
		view = FileSystemView.getFileSystemView();
	}
	
	/**
	 * <p>Copy a <code>File</code> object to the internal clip-board.
	 * The file can be pasted using <code>pasteFile</code>.</p>
	 * @param f
	 * @return A <code>File</code> object representing the copied file if
	 * successful. Otherwise, returns null.
	 */
	public File copyToClip(File f) {
		if (f.exists()) {
			clipFile = f;
			return f;
		}
		
		return null;
	}

	/**
	 * <p>The actual copy operation for the public copyFile method.</p>
	 * @param srcFile
	 * @param dstFile
	 */
	private void copyFile(File srcFile, File dstFile) throws IOException {
		FileInputStream fin;
		FileOutputStream fout;
		FileChannel srcCh = null;
		FileChannel dstCh = null;

		dstFile.createNewFile();
		fin = new FileInputStream(srcFile);
		fout = new FileOutputStream(dstFile);
		srcCh = fin.getChannel();
		dstCh = fout.getChannel();
		dstCh.transferFrom(srcCh, 0, srcCh.size());
		
		fin.close();
		fout.close();
		srcCh.close();
		dstCh.close();
		
		return;
	}
	
	/**
	 * <p>Copies directories properly.</p>
	 * @throws IOException
	 */
	private void copyDir() throws IOException { //TODO
		return;
	}

	// TODO: Fix Paste function so that it can paste directories.
	/**
	 * <p>Copies the file named by <code>srcFile</code> to
	 * the location named by <code>dstFile</code>.</p>
	 * @param srcFile The file to be copied
	 * @param dstFile The destination for the copied file
	 * @param owFlag If true, overwrite an existing file
	 */
	public void copyFile(File srcFile, File dstFile, boolean owFlag) throws IOException {
		// used to monitor the directory contents when copying a directory.
		File[] dirContents;
		// used in copying srcFile to pasteFile
		FileInputStream fin;
		FileOutputStream fout;
		FileChannel srcCh = null;
		FileChannel dstCh = null;

//		if (srcFile.isDirectory() && dstFile.isDirectory()) {
////			if (!dstFile.isDirectory()) {
////				throw new IOException("Error: " + srcFile.getPath() + " is a " +
////			"directory, while " + dstFile.getPath() + " is not.");
////			}
//			if (!dstFile.exists()) {
//				dstFile.mkdir();
//			}
//			// now copy all of the files inside of clipFile into pasteFile
//			dirContents = view.getFiles(srcFile, false);
//			for (int i = 0; i < dirContents.length; i++) {
//				try {
//					pasteFile(dirContents[i],
//							new File(dstFile.getPath() + File.separator +
//									dirContents[i].getName()));
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.err.println(e.getMessage());
//				}
//			}
//		}
//		else if (!srcFile.isDirectory() && dstFile.isDirectory()) {
//			dstFile = new File(dstFile.getPath() + File.separator + srcFile.getName());
//			try {
//				pasteFile(srcFile, dstFile);
//			} catch (IOException e) {
//				e.printStackTrace();
//				System.err.println(e.getMessage());
//			}
//		}
//		else if (!srcFile.isDirectory() && !dstFile.isDirectory()) {
////			if (dstFile.isDirectory()) {
////				throw new IOException("Error: " + srcFile.getPath() + " is not a " +
////			"directory, while " + dstFile.getPath() + "is a directory.");
////			}
//			dstFile.createNewFile();
//			fin = new FileInputStream(srcFile);
//			fout = new FileOutputStream(dstFile);
//			srcCh = fin.getChannel();
//			dstCh = fout.getChannel();
//			dstCh.transferFrom(srcCh, 0, srcCh.size());
//
//			/*
//			 * clean up - now that the file has been pasted, we close all resources
//			 * and nullify clipFile.
//			 */
//			fin.close();
//			fout.close();
//			srcCh.close();
//			dstCh.close();
//		}
//		else if (srcFile.isDirectory() && dstFile.exists() && !dstFile.isDirectory()) {
//			throw new IOException("Error: " + srcFile.getPath() + " is a " +
//		"directory, while " + dstFile.getPath() + " is not.");
//		}
//		if (srcFile.equals(clipFile))
//			clipFile = null;
		
		// Try again below...
		if (srcFile.equals(dstFile) && !owFlag) {
			throw new IOException("Error: " + srcFile.getName()
					+ " already exists in this location.");
		}
		else if(srcFile.isFile()) {
			if(dstFile.isFile()) {
				if (dstFile.exists()) {
					if (!owFlag)
						throw new IOException();
					else // owFlag is set
						copyFile(srcFile, dstFile); // do copy
				}
				else // dstFile does not exist
					copyFile(srcFile, dstFile); // do copy
			}
			else if(dstFile.isDirectory()) {
				if (new File(dstFile.getPath() + File.pathSeparator
						+ srcFile.getName()).exists()) { // file already exists inside of dstFile/
					if (!owFlag)
						throw new IOException("Error: " + srcFile.getName()
								+ " already exists in this location.");
					else // owFlag is set
						copyFile(srcFile, dstFile); // do copy
				}
				else // file does not already exist inside of dstFile/
					copyFile(srcFile, dstFile); // do copy
				
			}
			else throw new IOException("Error: " + srcFile.getName()
					+ " is neither a regular file nor a directory.");
		}
		else if(srcFile.isDirectory()) {
			
		}
		
		return;
	}

	// TODO: Finish Delete function so that it can delete non-empty directories.
	/**
	 * <p>Deletes the <code>File</code> object named by selecting the appropriate
	 * term in the main <code>JList</code> object. It always asks for confirmation
	 * via a <code>JOptionPane</code> before deleting.</p>
	 * @param f
	 */
	public void deleteFile(File f) {
		/*
		 * If f is a directory, we should delete recursively. Enter the directory
		 * and delete all of its contents first, then delete the directory itself.
		 * 
		 * See http://stackoverflow.com/questions/5930087/how-to-check-if-a-directory-is-empty-in-java
		 * for more info.
		 */
		if (f.isDirectory()) {
			if (view.getFiles(f, false).equals(null))
				f.delete();
			else
				System.out.println("Non-empty directory");
		}
		else {
			f.delete();
		}
	}
	
	/**
	 * <p>Creates a new directory within the current directory.</p>
	 */
	public void mkdir(File parent) {
		if (!(new File(parent + File.separator + "Untitled").mkdir()))
			System.err.println("Could not create directory.");
	}

	/**
	 * <p>Opens the file specified by the <code>File</code> object <code>f</code>.
	 * If <code>f</code> is a directory, move to that directory. Otherwise, try to
	 * open through the OS.</p>
	 * @param f
	 */
	public void openFile(File f) throws IOException {
		if (f.isDirectory()) {
				dir = f;
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
	

}
