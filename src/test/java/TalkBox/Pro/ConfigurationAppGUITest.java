package test.java.TalkBox.Pro;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.lang.String;
import javax.swing.DefaultListModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import main.java.TalkBox.Pro.TalkBoxConfigurationGUI.ConfigurationAppGUI;
import main.java.TalkBox.Pro.TalkBoxConfigurationGUI.SoundEngine;
import main.java.TalkBox.Pro.TalkBoxConfigurationGUI.TalkBoxConfiguration;

import java.lang.Thread;

class ConfigurationAppGUITest {

	ConfigurationAppGUI gui;
	TalkBoxConfiguration talkbox;

	@BeforeEach
	public void setUp() throws Exception {
		Thread.sleep(100);
		gui = new ConfigurationAppGUI();
	}

	/**
	 * Testing the findFiles method from the ConfigurationAppGUI.java class. Checks
	 * if the method returns the correct files when a suffix is passed.
	 */

	@Disabled
	@Test
	public void testFindFiles() {
		int i = 0;
		int j = 0;
		File sFile = new File("Sounds");
		assertTrue(sFile.isDirectory());

		String[] wavFiles = new String[sFile.listFiles().length];
		String[] allFiles = new String[sFile.listFiles().length];

		File[] files1 = sFile.listFiles();

		for (File file : files1) {
			if (file.getName().endsWith("wav")) {
				wavFiles[i] = file.getName();
				i++;
			}

			allFiles[j] = file.getName();
			j++;

		}

		String[] files = ConfigurationAppGUI.findFiles("Sounds", "wav");
		String[] allFiles2 = ConfigurationAppGUI.findFiles("Sounds", null);

		// Test if method correctly find only .wav files and all files respectively

		assertArrayEquals(files, wavFiles);
		assertArrayEquals(allFiles, allFiles2);

		// assert method returns null when an incorrect directory is entered

		assertNull(ConfigurationAppGUI.findFiles("Not a Directory", "wav"));

	}

	/**
	 * Testing the Play button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testClickPlayButton() throws InterruptedException {
		Thread.sleep(1000);
		File sFile = new File("Sounds");
		File[] files = sFile.listFiles();
		selectFile(1);
		gui.player.loadSound(files[1]);
		long duration = gui.player.currentSoundClip.getMicrosecondLength();
		System.out.println(duration);
		clickPlay();
		assertTrue(isPlaying());
		Thread.sleep((long) ((duration / 1000) + 200));
		assertFalse(isPlaying());
		Thread.sleep(1000);
	}

	/**
	 * Testing the Stop button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	// @Disabled
	@Test
	public void testClickStopButton() throws InterruptedException {
		Thread.sleep(1000);
		selectFile(1);
		clickPlay();

		Thread.sleep(700);

		clickStop();
		assertNull(gui.player.currentSoundClip);

	}

	/**
	 * Testing the Pause button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testClickPauseButton() throws InterruptedException {
		Thread.sleep(100);
		selectFile(1);
		clickPlay();
		Thread.sleep(700);

		clickPause();
		Thread.sleep(100);
	}

	/**
	 * Testing the Resume button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testClickResumeButton() throws InterruptedException {
		Thread.sleep(100);
		// play third audio file in list
		selectFile(2);
		clickPlay();
		Thread.sleep(600);
		clickPause();
		Thread.sleep(500);
		// resume currently paused audio file
		clickResume();
		Thread.sleep(100);
	}

	/**
	 * Testing the Reset button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testClickResetButton() throws InterruptedException {
		Thread.sleep(100);
		// click reset button
		clickReset();

		assertEquals(gui.getNumberOfAudioSets(), 4);
		assertEquals(gui.getNumberOfAudioButtons(), 3);
		String[][] defaultArray = gui.getAudioFileNames();
		// add two new buttons
		clickAddButton();
		clickAddButton();
		// add first and second elements to final list
		selectFile(0);
		clickAddButton();
		selectFile(1);
		clickAddButton();
		clickReset();
		String[][] resetArray = gui.getAudioFileNames();

		assertArrayEquals(defaultArray, resetArray);
		assertTrue(gui.finalListModel.isEmpty());
		assertEquals(gui.getNumberOfAudioSets(), 4);
		assertEquals(gui.getNumberOfAudioButtons(), 3);
		Thread.sleep(100);
	}

	/**
	 * Testing the Swap button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testClickSwapButton() throws InterruptedException {
		Thread.sleep(100);
		// get file names for audio files
		int j = 0;
		File soundFile = new File("Sounds");
		File[] files1 = soundFile.listFiles();
		String[] allFiles = new String[soundFile.listFiles().length];
		for (File file : files1) {
			allFiles[j] = file.getName();
			++j;
		}
		String[] initialList = { allFiles[0], allFiles[1], allFiles[2] };

		clickReset();
		selectFile(3);
		clickAddButton();
		selectFile(2);
		clickAddButton();
		selectFile(0);
		clickAddButton();

		assertArrayEquals(initialList, gui.finalListModel.toArray());

		int k = 3;

		// change initial list to next 3 audio files
		for (int i = 0; i <= 2; i++) {
			initialList[i] = allFiles[k];
			k++;
		}
		// press swap button
		clickSwap();
		assertArrayEquals(initialList, gui.finalListModel.toArray());

		// change initial list to next 3 audio files
		for (int i = 0; i <= 2; i++) {
			initialList[i] = allFiles[k];
			k++;
		}
		clickSwap();
		assertArrayEquals(initialList, gui.finalListModel.toArray());

		// change list to next 3 audio files, if reach end of audio files go back to
		// beginning
		for (int i = 0; i <= 2; i++) {
			if (k == files1.length) {
				k = 0;
			}

			initialList[i] = allFiles[k];
			k++;
		}
		clickSwap();
		Thread.sleep(100);
		assertArrayEquals(initialList, gui.finalListModel.toArray());
		Thread.sleep(100);
	}

	/**
	 * Testing the Save Changes button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testSaveChangesButton() throws InterruptedException {
		Thread.sleep(100);
		clickReset();

		// add arbitrary audio files to final list
		selectFile(4);
		clickAddButton();

		clickSave();
		// check if any changes made to initial list
		if (gui.initialListModel.toArray() == gui.finalListModel.toArray()) {
			fail();
		}

		selectFile(2);
		clickAddButton();
		selectFile(6);
		clickAddButton();

		// store final list
		DefaultListModel<?> initialFinalList = gui.finalListModel;

		Thread.sleep(1000);

		clickSave();

		Thread.sleep(1000);
		// compare new initial list with old final list
		assertArrayEquals(gui.initialListModel.toArray(), initialFinalList.toArray());

		// open new TalkBox after saving
		ConfigurationAppGUI gui2 = new ConfigurationAppGUI();

		assertArrayEquals(gui2.initialListModel.toArray(), initialFinalList.toArray());

	}

	/**
	 * Testing the Add Initial button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testAddInitialButton() throws InterruptedException {
		Thread.sleep(100);
		clickReset();
		// i is size of current initial list
		int i = gui.initialListModel.getSize();
		clickAddButton();

		assertEquals(gui.initialListModel.getSize(), i + 1);
		i++;

		clickAddButton();

		clickAddButton();

		assertEquals(gui.initialListModel.getSize(), i + 2);

		clickReset();

		// add the remaining 7 available buttons to initial list

		File soundFile = new File("Sounds");
		File[] files1 = soundFile.listFiles();

		for (int j = 0; j < files1.length - gui.orderButtons.length; j++) {
			clickAddButton();
		}

		assertEquals(gui.initialListModel.getSize(), files1.length);

		clickAddButton();

		// ensure no changes has been made after attempting to add too many buttons
		assertEquals(gui.initialListModel.getSize(), gui.getNumberOfAudioButtons());
		assertEquals(gui.initialListModel.getSize(), files1.length);

		Thread.sleep(100);

	}

	/**
	 * Testing the Remove Initial button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testRemoveInitialButton() throws InterruptedException {
		Thread.sleep(100);
		clickReset();
		clickAddButton();
		clickAddButton();
		clickAddButton();

		int i = gui.initialListModel.getSize();

		clickRemoveButton();

		assertEquals(gui.initialListModel.getSize(), i - 1);
		i--;

		clickRemoveButton();
		clickRemoveButton();

		assertEquals(gui.initialListModel.getSize(), i - 2);

		clickRemoveButton();

		assertEquals(gui.initialListModel.getSize(), 3);
		Thread.sleep(100);
	}

	/**
	 * Testing the Add Final button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testAddFinalButton() throws InterruptedException {
		Thread.sleep(100);
		int i = 0;
		clickReset();
		// add 2 files to final list
		selectFile(0);
		clickAddButton();
		selectFile(1);
		clickAddButton();

		assertEquals(gui.finalListModel.getSize(), i + 2);

		// attempt to add same audio file to final list
		clickAddButton();

		if (gui.finalListModel.getSize() > i + 2) {
			fail();
		}

		selectFile(2);
		clickAddButton();
		assertEquals(gui.finalListModel.getSize(), i + 3);

		// attempt to add more files than there are buttons
		selectFile(3);
		clickAddButton();

		if (gui.finalListModel.getSize() > gui.initialListModel.getSize()) {
			fail();
		}
		Thread.sleep(100);
	}

	/**
	 * Testing the Remove Final button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testRemoveFinalButton() throws InterruptedException {
		Thread.sleep(100);
		clickReset();

		// add 3 files to the final list
		for (int i = 0; i < 3; i++) {
			selectFile(i);
			clickAddButton();
		}

		int finalListSize = gui.finalListModel.getSize();

		clickRemoveButton();
		assertEquals(gui.finalListModel.getSize(), finalListSize - 1);

		// remove two files and test size
		finalListSize -= 1;
		clickRemoveButton();
		clickRemoveButton();
		assertEquals(gui.finalListModel.getSize(), finalListSize - 2);

		// attempt to remove files when list is empty
		clickRemoveButton();
		if (gui.finalListModel.getSize() < 0) {
			fail();
		}
		Thread.sleep(100);
	}

	/**
	 * Testing the Launch Simulator button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testLaunchSimulator() throws InterruptedException {
		Thread.sleep(100);
		clickReset();
		clickLaunchSim();
		Thread.sleep(100);
		assertTrue(gui.myFrame.isActive());
		assertTrue(gui.myFrame.isVisible());
		assertEquals(gui.myFrame.getNumberOfAudioButtons(), 3);
		assertEquals(gui.myFrame.getNumberOfAudioSets(), 4);
		assertEquals(gui.myFrame.getTotalNumberOfButtons(), 12);
		Thread.sleep(100);

		gui.myFrame.swapButtons[0].doClick();
		gui.myFrame.audioButtons[0].doClick();

		Thread.sleep(100);

	}

	private void clickLaunchSim() {
		gui.launchSimApp.doClick();
	}

	private void clickPlay() {
		gui.playBtn.doClick();
	}

	private void selectFile(int index) {
		gui.audioList.setSelectedIndex(index);
	}

	private void clickStop() {
		gui.stopBtn.doClick();
	}

	private void clickPause() {
		gui.pauseBtn.doClick();
	}

	private void clickResume() {
		gui.resumeBtn.doClick();
	}

	private void clickReset() {
		gui.resetBtn.doClick();
	}

	private void clickAddButton() {
		gui.addFinalBtn.doClick();
	}

	private void clickSwap() {
		gui.swapBtn.doClick();
	}

	private void clickSave() {
		gui.saveChangesBtn.doClick();
	}

	private void clickRemoveButton() {
		gui.removeNewBtn.doClick();
	}

	private boolean isPlaying() {
		return gui.player.isPlaying();
	}

}
