package filesystem3;

/**
 * <p>Java Disk Explorer. A portable, graphical, Java-based file manager.</p>
 * <p>Java Disk Explorer, or JDX, provides an easy-to-use file management
 * interface with a uniform look and feel across multiple OS environments.</p>
 * <p>Right now, it's in its infancy, but it will grow with time! Patience!</p>
 * 
 * @author Ken Peck
 * @date   November 24th, 2014
 * 
 * <p>Copyright (c) 2014 Ken Peck.</p>
 */
public class JavaDiskExplorer {
	private static final String version = "6.0.0b";

	public static void main(String[] args) {
		FileViewerWindow mainWin = new FileViewerWindow("Java Disk Explorer v" +
														version);

		// enable the window and set default size.
		mainWin.setVisible(true);
		mainWin.setSize(640, 480);
	}
}
