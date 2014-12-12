package filesystem3;

/**
 * <p>JavaDiskExplorer</p>
 * <p>Description: CS343 Group Project</p>
 * @author Ken Peck
 * @author Jason Skinner
 * @author Robbie Fox
 * @date   November 24th, 2014
 */
public class JavaDiskExplorer {
	public static void main(String[] args) {
		FileViewerWindow mainWin = new FileViewerWindow();

		// enable the window and set default size.
		mainWin.setVisible(true);
		mainWin.setSize(640, 480);
	}
}
