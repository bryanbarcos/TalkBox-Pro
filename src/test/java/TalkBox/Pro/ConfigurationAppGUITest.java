
package test.java.TalkBox.Pro;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.String;
import javax.swing.DefaultListModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import main.java.TalkBox.Pro.ConfigurationAppGUI;
import main.java.TalkBox.Pro.SoundEngine;
import main.java.TalkBox.Pro.TalkBoxConfiguration;

class ConfigurationAppGUITest {

	ConfigurationAppGUI gui = new ConfigurationAppGUI();
	SoundEngine player;
	TalkBoxConfiguration talkbox;

	@BeforeEach
	public void setUp() throws Exception {
		Thread.sleep(100);
		gui = new ConfigurationAppGUI();
		Thread.sleep(100);
	} 

	/**
	 * Testing the findFiles method from the ConfigurationAppGUI.java class. Checks
	 * if the method returns the correct files when a suffix is passed.
	 */

	//@Disabled
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

		// assert method returns null when an incorrect direct

		assertNull(ConfigurationAppGUI.findFiles("Not a Directory", "wav"));

	}

	/**
	 * Testing the Play button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	//@Disabled
	@Test
	public void testClickPlayButton() throws InterruptedException {
		Thread.sleep(1000);
		//click play button
		gui.playBtn.doClick();
		Thread.sleep(1000);
	}

	/**
	 * Testing the Stop button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */

	//@Disabled
	@Test
	public void testClickStopButton() throws InterruptedException {
		Thread.sleep(1000);
		// play the second audio file in the audio list
		gui.audioList.setSelectedIndex(1);
		gui.playBtn.doClick();
		Thread.sleep(700);
		// stop currently playing audio file
		gui.stopBtn.doClick();
		Thread.sleep(1000);
	}

	/**
	 * Testing the Pause button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */
	
	//@Disabled
	@Test
	public void testClickPauseButton() throws InterruptedException {
		Thread.sleep(100);
		// play second audio file in the audio list
		gui.audioList.setSelectedIndex(1);
		gui.playBtn.doClick();
		Thread.sleep(700);
		// pause currently playing audio file
		gui.pauseBtn.doClick();
		Thread.sleep(100);
	}

	/**
	 * Testing the Resume button in the Configruation App
	 * 
	 * @throws InterruptedException
	 */
	
	//@Disabled
	@Test
	public void testClickResumeButton() throws InterruptedException {
		Thread.sleep(100);
		// play third audio file in list
		gui.audioList.setSelectedIndex(2);
		gui.playBtn.doClick();
		Thread.sleep(600);
		// pause currently playing audio file
		gui.pauseBtn.doClick();
		Thread.sleep(500);
		// resume currently paused audio file
		gui.resumeBtn.doClick();
		Thread.sleep(100);
	}

	/**
	 * Testing the Reset button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */
	
	//@Disabled
	@Test
	public void testClickResetButton() throws InterruptedException {
		Thread.sleep(100);
		// click reset button
		gui.resetBtn.doClick();

		assertEquals(gui.getNumberOfAudioSets(), 4);
		assertEquals(gui.getNumberOfAudioButtons(), 3);
		String[][] defaultArray = gui.getAudioFileNames();
		// add two new buttons
		gui.addNewBtn.doClick();
		gui.addNewBtn.doClick();
		// add first and second elements to final list
		gui.audioList.setSelectedIndex(0);
		gui.addFinalBtn.doClick();
		gui.audioList.setSelectedIndex(1);
		gui.addFinalBtn.doClick();
		// click reset button
		gui.resetBtn.doClick();
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
	
	//@Disabled
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

		// add first 3 audio files to final list
		gui.resetBtn.doClick();
		gui.audioList.setSelectedIndex(2);
		gui.addFinalBtn.doClick();
		gui.audioList.setSelectedIndex(1);
		gui.addFinalBtn.doClick();
		gui.audioList.setSelectedIndex(0);
		gui.addFinalBtn.doClick();

		assertArrayEquals(initialList, gui.finalListModel.toArray());

		int k = 3;

		// change initial list to next 3 audio files
		for (int i = 0; i <= 2; i++) {
			initialList[i] = allFiles[k];
			k++;
		}
		// press swap button
		gui.swapBtn.doClick();
		assertArrayEquals(initialList, gui.finalListModel.toArray());

		// change initial list to next 3 audio files
		for (int i = 0; i <= 2; i++) {
			initialList[i] = allFiles[k];
			k++;
		}
		// press swap button again
		gui.swapBtn.doClick();
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
		// press swap button again
		gui.swapBtn.doClick();
		Thread.sleep(100);
		assertArrayEquals(initialList, gui.finalListModel.toArray());
		Thread.sleep(100);
	}

	/**
	 * Testing the Save Changes button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */
	
	//@Disabled
	@Test
	public void testSaveChangesButton() throws InterruptedException {
		Thread.sleep(100);
		// click reset button
		gui.resetBtn.doClick();

		// add arbitrary audio files to final list
		gui.audioList.setSelectedIndex(4);
		gui.addFinalBtn.doClick();

		gui.saveChangesBtn.doClick();
		// check if any changes made to initial list
		if (gui.initialListModel.toArray() == gui.finalListModel.toArray()) {
			fail();
		}

		gui.audioList.setSelectedIndex(2);
		gui.addFinalBtn.doClick();
		gui.audioList.setSelectedIndex(6);
		gui.addFinalBtn.doClick();

		// store final list
		DefaultListModel<?> initialFinalList = gui.finalListModel;

		Thread.sleep(1000);
		
		// click save changes button
		gui.saveChangesBtn.doClick();

		
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
	
	//@Disabled
	@Test
	public void testAddInitialButton() throws InterruptedException {
		Thread.sleep(100);
		// reset to default
		gui.resetBtn.doClick();
		// i is size of current initial list
		int i = gui.initialListModel.getSize();
		gui.addNewBtn.doClick();

		assertEquals(gui.initialListModel.getSize(), i + 1);
		i++;

		gui.addNewBtn.doClick();

		gui.addNewBtn.doClick();

		assertEquals(gui.initialListModel.getSize(), i + 2);

		// set back to default, 3 audio files in initial list
		gui.resetBtn.doClick();

		// add the remaining 7 available buttons to initial list

		File soundFile = new File("Sounds");
		File[] files1 = soundFile.listFiles();
	
		for (int j = 0; j < files1.length - gui.orderButtons.length; j++) {
			gui.addNewBtn.doClick();
		}

		assertEquals(gui.initialListModel.getSize(), files1.length);

		gui.addNewBtn.doClick();

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
	
	//@Disabled
	@Test
	public void testRemoveInitialButton() throws InterruptedException {
		Thread.sleep(100);
		gui.resetBtn.doClick();
		// buttons added
		gui.addNewBtn.doClick();
		gui.addNewBtn.doClick();
		gui.addNewBtn.doClick();

		int i = gui.initialListModel.getSize();

		gui.removeNewBtn.doClick();

		assertEquals(gui.initialListModel.getSize(), i - 1);
		i--;

		gui.removeNewBtn.doClick();
		gui.removeNewBtn.doClick();

		assertEquals(gui.initialListModel.getSize(), i - 2);

		gui.removeNewBtn.doClick();

		assertEquals(gui.initialListModel.getSize(), 3);
		Thread.sleep(100);
	}

	/**
	 * Testing the Add Final button in the Configuration App
	 * 
	 * @throws InterruptedException
	 */
	
	//@Disabled
	@Test
	public void testAddFinalButton() throws InterruptedException {
		Thread.sleep(100);
		int i = 0;
		// make final list empty
		gui.resetBtn.doClick();
		// add 2 files to final list
		gui.audioList.setSelectedIndex(0);
		gui.addFinalBtn.doClick();
		gui.audioList.setSelectedIndex(1);
		gui.addFinalBtn.doClick();

		assertEquals(gui.finalListModel.getSize(), i + 2);

		// attempt to add same audio file to final list
		gui.addFinalBtn.doClick();

		if (gui.finalListModel.getSize() > i + 2) {
			fail();
		}

		gui.audioList.setSelectedIndex(2);
		gui.addFinalBtn.doClick();
		assertEquals(gui.finalListModel.getSize(), i + 3);

		// attempt to add more files than there are buttons
		gui.audioList.setSelectedIndex(3);
		gui.addFinalBtn.doClick();

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
	
	//@Disabled
	@Test
	public void testRemoveFinalButton() throws InterruptedException {
		Thread.sleep(100);
		gui.resetBtn.doClick();

		// add 3 files to the final list
		for (int i = 0; i < 3; i++) {
			gui.audioList.setSelectedIndex(i);
			gui.addFinalBtn.doClick();
		}

		int finalListSize = gui.finalListModel.getSize();

		gui.removeFinalBtn.doClick();
		assertEquals(gui.finalListModel.getSize(), finalListSize - 1);

		// remove two files and test size
		finalListSize -= 1;
		gui.removeFinalBtn.doClick();
		gui.removeFinalBtn.doClick();
		assertEquals(gui.finalListModel.getSize(), finalListSize - 2);

		// attempt to remove files when list is empty
		gui.removeFinalBtn.doClick();
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
	
	//@Disabled
	@Test
	public void testLaunchSimulator() throws InterruptedException {
		Thread.sleep(100);
		gui.resetBtn.doClick();
		gui.launchSimApp.doClick();
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

}


