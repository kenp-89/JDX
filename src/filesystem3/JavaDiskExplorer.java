package filesystem3;

/**
 * <p>JavaDiskExplorer</p>
 * <p>Description: CS343 Group Project</p>
 * @author Ken Peck <ken.peck@my.uwrf.edu>
 * @author Jason Skinner <jason.skinner@my.uwrf.edu>
 * @author Robbie Fox <robert.fox@my.uwrf.edu>
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
