package simulatorGUI;

import TalkBoxConfigurationGUI.SaveData;
import TalkBoxConfigurationGUI.TalkBoxConfiguration;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class SimulatorApp extends JFrame
		implements TalkBoxConfiguration {
	private static final String VERSION = "Version 1.0";
	private static final String AUDIO_DIR = Paths.get(".\\Sounds").toString();
	//private static final String AUDIO_DIR = new File("/Sounds").toURI().relativize(new File("X:/York 2/EECS2311/TalkBox-Pro/Sounds").toURI()).getPath();
	private static final String fileName = "TalkBoxConfig.txt";

	private SoundEngine player;
	public JButton[] audioButtons;
	public JButton[] swapButtons;
	JList finalAudioList;
	int numberOfAudioButtons;
	int numberOfAudioSets;
	int totalNumberOfButtons;
	public String[][] audioFileNames;

	public static void main(String[] args) 
	{
		SimulatorApp gui = new SimulatorApp();
	}
	public SimulatorApp() {
		super("TalkBox");
		player = new SoundEngine();
		try {
			//Deserialization
			ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
			SaveData data = (SaveData) ois.readObject();

			//Reading values from TalkBoxConfig file.
			finalAudioList = data.finalList;
			numberOfAudioButtons = data.numberOfAudioButtons;
			numberOfAudioSets = data.numberOfAudioSets;
			totalNumberOfButtons = data.totalNumberOfButtons;
			audioFileNames = data.audioFileNames;
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		//String[] audioFileNames = ConfigurationAppGUI.findFiles(AUDIO_DIR, null);
		audioButtons = new JButton[numberOfAudioButtons];
		swapButtons = new JButton[numberOfAudioSets];
		makeFrame();
	}
	public void makeFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		JPanel audio = new JPanel();
		JPanel swap = new JPanel();
		contentPane.setBorder(new EmptyBorder(6, 10, 10, 10));
		contentPane.setSize(new Dimension(800, 800));
		contentPane.setLayout(new BorderLayout());
		//Add audio Buttons
		for(int i = 0; i < numberOfAudioButtons; i++) {
			audioButtons[i] = new JButton(finalAudioList.getModel().getElementAt(i).toString());
			int k = i;
			audioButtons[i].addActionListener(e -> {
				String filename = audioButtons[k].getText();
				player.play(new File(AUDIO_DIR, filename));
			});
			audio.add(audioButtons[i]);
		}
		contentPane.add(audio, BorderLayout.NORTH);
		//Add Swap Buttons
		for(int i = 0; i < numberOfAudioSets; i++){
			swapButtons[i] = new JButton("Swap: " + (i + 1));
			int k = i;
			swapButtons[i].addActionListener(e -> {getSwapAudioSets(k);});
			swap.add(swapButtons[i]);
		}
		contentPane.add(swap, BorderLayout.CENTER);
		getContentPane().add(contentPane);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(d.width/2 - getWidth()/2, d.height/2 - getHeight()/2);
		pack();
		setVisible(true);
	}

	public void getSwapAudioSets(int k)
	{
		System.out.println(Arrays.deepToString(audioFileNames).replace("], ", "]\n"));
		for(int i = 0; i < audioFileNames[0].length; i++) {
			audioButtons[i].setText(audioFileNames[k][i]);
		}
	}

	@Override
	public int getNumberOfAudioButtons() {
		return numberOfAudioButtons;
	}

	@Override
	public int getNumberOfAudioSets() {
		return numberOfAudioSets;
	}

	@Override
	public int getTotalNumberOfButtons() {
		return totalNumberOfButtons;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		return null;
	}

	@Override
	public String[][] getAudioFileNames() {
		return audioFileNames;
	}
}
