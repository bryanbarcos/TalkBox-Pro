package test.java.TalkBox.Pro;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.lang.String;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

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

	@Disabled
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
		Thread.sleep(400);

		clickPause();
		assertFalse(isPlaying());
		Thread.sleep(100);
		clickResume();
		assertTrue(isPlaying());
		clickStop();
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

		clickPause();

		// resume currently paused audio file
		clickResume();

		assertTrue(isPlaying());

		clickStop();
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

		if (hasSavedData()) {

			assertEquals(gui.initialListModel.getSize(), gui.finalListModel.getSize());
			clickReset();
			assertEquals(gui.initialListModel.getSize(), gui.finalListModel.getSize());

			selectFile(6);
			gui.yesOrNo = gui.YES;
			clickAddButton();
			assertNotEquals(gui.initialListModel.getSize(), gui.finalListModel.getSize());

			clickReset();

			assertEquals(gui.initialListModel.getSize(), gui.finalListModel.getSize());

		}

		else {
			assertEquals(gui.initialListModel.getSize(), 0);

		}
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
		int buttons = gui.getNumberOfAudioButtons();
		int k = gui.getNumberOfAudioButtons();
		int j = 0;
		File soundFile = new File("Sounds");
		File[] files1 = soundFile.listFiles();
		String[] allFiles = new String[soundFile.listFiles().length];
		for (File file : files1) {
			allFiles[j] = file.getName();
			++j;
		}
		String[] initialList = new String[k];

			for (int i = 0; i < buttons; i++) {
				if (k == files1.length) {
					k = 0;
				}
				initialList[i] = allFiles[k];
				k++;
			}
			clickSwap();
			assertArrayEquals(initialList, gui.finalListModel.toArray());
			
			for (int i = 0; i < buttons; i++) {
				if (k == files1.length) {
					k = 0;
				}
				initialList[i] = allFiles[k];
				k++;
			}
			clickSwap();
			assertArrayEquals(initialList, gui.finalListModel.toArray());
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

		if (hasSavedData()) {
			DefaultListModel<?> savedData = gui.finalListModel;
			
			if (savedData.size() > 3) {
				
				clickRemoveButton();
				clickSave();
			}
			
			else {
				selectFile(6);
				clickAddButton();
				selectFile(9);
				clickAddButton();
				clickSave();
			}
			DefaultListModel<?> initialFinalList = gui.finalListModel;
			assertArrayEquals(gui.initialListModel.toArray(), initialFinalList.toArray());

			// open new TalkBox after saving
			ConfigurationAppGUI gui2 = new ConfigurationAppGUI();

			assertArrayEquals(gui2.initialListModel.toArray(), initialFinalList.toArray());
			gui.finalListModel = savedData;
		}
		
		else {
			selectFile(4);
			clickAddButton();
			clickSave();
			
			DefaultListModel<?> initialFinalList = gui.finalListModel;
			assertArrayEquals(gui.initialListModel.toArray(), initialFinalList.toArray());

			// open new TalkBox after saving
			ConfigurationAppGUI gui2 = new ConfigurationAppGUI();

			assertArrayEquals(gui2.initialListModel.toArray(), initialFinalList.toArray());
			deleteSaved();
		}
	}

	/**
	 * Testing the Add Final button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	@Disabled
	@Test
	public void testAddButton() throws InterruptedException {
		Thread.sleep(100);
		int i = 0;
		
		if (hasSavedData()) {
			
		}
		
		else {

			selectFile(4);
			clickAddButton();
			selectFile(5);
			clickAddButton();
			assertEquals(gui.finalListModel.getSize(), i + 2);
			clickAddButton();
		}
		

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
	public void testRemoveButton() throws InterruptedException {
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
		gui.removeFinalBtn.doClick();
	}

	private void clickResume() {
		gui.pauseBtn.doClick();
	}

	private boolean isPlaying() {
		return gui.player.isPlaying();
	}

	private boolean hasSavedData() {
		File sFile = new File("TalkBoxConfig.txt");
		return sFile.exists();
	}
	
	private void deleteSaved() {
		File sFile = new File("TalkBoxConfig.txt");
		sFile.delete();
	}

}
