package filesystemTests;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import filesystem3.FileViewerWindow;

/*
 * Note: I've discovered that FileViewerWindow is actually quite difficult to test
 * in a standard way. Almost all of the methods are private (they aren't meant to be
 * used outside of FileViewerWindow), AND almost all of them return void,
 * so this is an attempt to test the event-handler, and its effects on private
 * data members. It will probably be a bit wonky.
 * 
 * -Ken
 */
/**
 * <p>FileViewerWindowTests</p>
 * <p>Description: CS343 Group Project</p>
 * @author Ken Peck
 * @author Jason Skinner
 *
 * <p>Tests for the <code>FileViewerWindow</code> class.</p>
 * <p>Written by Ken Peck and Jason Skinner.</p>
 */
public class FileViewerWindowTests {
	private FileViewerWindow testWin;
	private FileSystemView view;
	private File selectedFile;
	private static File testDir;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDir = new File((FileSystemView.getFileSystemView()).getHomeDirectory().getPath() + File.separator + "FileViewerWindowTestDir");
		testDir.mkdir();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testDir.deleteOnExit();
	}

	@Before
	public void setUp() throws Exception {
		testWin = new FileViewerWindow();
		testWin.getFl().setSelectedIndex(0);
		selectedFile = testWin.getFl().getSelectedValue();
	}

	@After
	public void tearDown() throws Exception {
		testWin = null;
		selectedFile = null;
	}
	
	@Test
	public final void constructorTest() {
		assertNotEquals(testWin, null);
	}

	@Test
	public final void curlocEventTest() {
		testWin.actionPerformed(new ActionEvent(testWin.getCurloc(), 0, "Location Bar Test"));
		assertEquals(testWin.getCurloc().getText(), testWin.getDir());
	}

	@Test
	public final void homebtnEventTest() {
		testWin.actionPerformed(new ActionEvent(testWin.getHomebtn(), 0, "Home Button Test"));
		assertEquals(testWin.getView().getHomeDirectory().getPath(), testWin.getDir());
	}
	
	@Test
	public final void pdirbtnEventTest() {
		testWin.actionPerformed(new ActionEvent(testWin.getPdirbtn(), 0, "Parent Directory Button Test"));
		assertEquals(testWin.getView().getHomeDirectory().getParent(), testWin.getDir());
	}

	@Test
	public final void openbtnEventTest() {
		testWin.actionPerformed(new ActionEvent(testWin.getOpenbtn(), 0, "Open Button Test"));
		assertEquals(selectedFile, new File(testWin.getDir()));
	}
	
	@Test
	public final void copybtnEventTest() {
		testWin.actionPerformed(new ActionEvent(testWin.getCopybtn(), 0, "Copy Button Test"));
		assertEquals(selectedFile, testWin.getClipFile());
	}
}
