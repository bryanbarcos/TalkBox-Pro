package main.java.TalkBox.Pro.TalkBoxConfigurationGUI;

import javax.swing.*;

public class SaveData implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public JList finalList;
    public JComboBox order;
    public int numberOfAudioButtons;
    public int numberOfAudioSets;
    public int totalNumberOfButtons;
    public String relativePathToAudioFiles;
    public String[][] audioFileNames;
}
