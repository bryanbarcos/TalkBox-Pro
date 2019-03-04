package TalkBoxConfigurationGUI;

import simulatorGUI.SimulatorApp;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A simple TalkBox. To start, create an instance of this class.
 *
 * The TalkBox can play sound clips in WAV, AU and AIFF formats
 */
public class ConfigurationAppGUI extends JFrame
        implements ChangeListener, ActionListener, TalkBoxConfiguration
{
    private static final String VERSION = "Version 1.0";
    private static final String AUDIO_DIR = Paths.get(".\\Sounds").toString();
    //private static final String AUDIO_DIR = new File("/Sounds").toURI().relativize(new File("X:/York 2/EECS2311/TalkBox-Pro/Sounds").toURI()).getPath();
    private static final String fileName = "TalkBoxConfig.txt";

    JList audioList;
    private JSlider slider;
    private JLabel infoLabel;
    private SoundEngine player;
    private JList initialList;
    private JList finalList;
    private JComboBox <Integer> order;
    DefaultListModel initialListModel; 
    DefaultListModel finalListModel; 
    private DefaultListModel audioListModel;
    private Integer[] orderButtons = {1, 2, 3};
    private DefaultComboBoxModel orderModel;
    Component[] comp;
    int c = 0;

    JButton playBtn;
    JButton stopBtn;
    JButton pauseBtn;
    JButton resumeBtn;
    JButton resetBtn;
    JButton swapBtn;
    JButton saveChangesBtn;
    JButton addFinalBtn;
    JButton addNewBtn;
    JButton removeNewBtn;
    JButton removeFinalBtn;
    JButton launchSimApp;
    JScrollPane rightScrollPane;
    JScrollPane leftScrollPane;
    SimulatorApp myFrame;
    File sounds = new File(fileName);


    //Main method for starting the player from a command line.
    public static void main(String[] args){
        ConfigurationAppGUI gui = new ConfigurationAppGUI();
    } 

    //Create a TalkBox and display its GUI on screen.
    public ConfigurationAppGUI() {
        super("TalkBox");
        player = new SoundEngine();
        String[] audioFileNames = findFiles(AUDIO_DIR, null);
        initialListModel = new DefaultListModel();
        initialList = new JList(initialListModel);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            if(sounds.exists()) {
                //Deserialization
                SaveData data = (SaveData) ResourceManager.load(fileName);
                //Populating InitialList from TalkBoxConfig.save file.

                finalListModel = new DefaultListModel();
                finalList = new JList(finalListModel);

                for (int i = 0; i < data.finalList.getModel().getSize(); i++) {
                    initialListModel.addElement(data.finalList.getModel().getElementAt(i));
                    finalListModel.addElement(data.finalList.getModel().getElementAt(i));
                }

                //Populating Order ComboBox from TalkBoxConfig.save file.
                orderModel = new DefaultComboBoxModel();
                order = new JComboBox<>(orderModel);
                for (int i = 0; i < data.order.getModel().getSize(); i++)
                    orderModel.addElement(data.order.getModel().getElementAt(i));
            }
//            else {
//                initialListModel = new DefaultListModel();
//                initialList = new JList(initialListModel);
//            }
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
        makeFrame(audioFileNames);
    }

    /**
     * Play the sound file currently selected in the file list. If there is no
     * selection in the list, or if the selected file is not a sound file,
     * do nothing.
     */

     public void play() {

        String filename = (String)audioList.getSelectedValue();
        if(filename == null) {  // nothing selected
            return;
        }
        slider.setValue(0);
        boolean successful = player.play(new File(AUDIO_DIR, filename));
        if(successful) {
            showInfo(filename + " (" + player.getDuration() + " seconds)");
        }
        else {
            showInfo("Could not play file - unknown format");
        }
    }

    /**
     * Display information about a selected sound file (name and clip length).
     * @param message The message to display.
     */
    private void showInfo(String message){ infoLabel.setText(message);}

    //Stop the currently playing sound file (if there is one playing).
    private void stop(){ player.stop();}

    //Stop the currently playing sound file (if there is one playing).
    private void pause() { player.pause();}

    //Resume a previously suspended sound file.
    private void resume() { player.resume();}

    //Quit function: quit the application.
    public void quit() {System.exit(0);}

    //About function: show the 'about' box.
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "TalkBox\n" + VERSION,
                "About TalkBox",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Load the file names of all files in the given directory.
     * @param dirName Directory (folder) name.
     * @param suffix File suffix of interest.
     * @return The names of files found.
     */

    public static String[] findFiles(String dirName, String suffix)

    {
        File dir = new File(dirName);
        if(dir.isDirectory()) {
            String[] allFiles = dir.list();
            if(suffix == null)
                return allFiles;
            else {
                List<String> selected = new ArrayList<String>();
                for(String filename : allFiles) {
                    if(filename.endsWith(suffix))
                        selected.add(filename);
                }
                return selected.toArray(new String[selected.size()]);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Error: " + dirName + " must be a directory");
            return null;
        }
    }

    // ------- ChangeListener interface (for Slider) -------

    /**
     * ChangeListener method for slider changes. This method is called
     * when the slider value is changed by the user.
     * @param evt The event details.
     */
    public void stateChanged(ChangeEvent evt){player.seek(slider.getValue());}

    // ------- ActionListener interface (for ComboBox) -------

    /**
     * ActionListener method for changes of format combo box.
     * When this methosd is called, the user has made a new selection
     * in the combo box.
     * @param evt The event details.
     */
    public void actionPerformed(ActionEvent evt) {
        JComboBox cb = (JComboBox)evt.getSource();
        String format = (String)cb.getSelectedItem();
        if(format.equals("all formats"))
            format = null;
        audioList.setListData(findFiles(AUDIO_DIR, format));
    }

    // ---- Swing stuff to build the frame and all its components and menus ----

    /**
     * Create the complete application GUI.
     * @param audioFiles The file names to display.
     */
    private void makeFrame(String[] audioFiles) {
        // the following makes sure that our application exits when
        // the user closes its window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(new EmptyBorder(6, 10, 10, 10));
        contentPane.setPreferredSize(new Dimension(780, 500));
        makeMenuBar();

        // Specify the layout manager with nice spacing
        contentPane.setLayout(new BorderLayout(8, 8));

        // Create the left side with combobox and scroll list
        JPanel leftPane = new JPanel(); {
            leftPane.setLayout(new BorderLayout(8, 8));

            String[] formats = {"all formats", ".wav", ".au", ".aif"};

            // Create the combo box.
            JComboBox formatList = new JComboBox(formats);
            formatList.setBackground(Color.BLACK);
            formatList.setForeground(new Color(140, 171, 226));
            formatList.addActionListener(this);
            leftPane.add(formatList, BorderLayout.NORTH);

            // Create the scrolled list for file names
            audioListModel = new DefaultListModel();
            audioList = new JList(audioListModel);
            for(int i = 0; i < audioFiles.length; i++)
                audioListModel.addElement(audioFiles[i]);
            setBackground(audioList);
            JScrollPane scrollPane = new JScrollPane(audioList);
            scrollPane.setColumnHeaderView(new JLabel("Audio files"));
            leftPane.add(scrollPane, BorderLayout.CENTER);
            //leftPane.setBackground(Color.BLACK);
        }
        contentPane.add(leftPane, BorderLayout.CENTER);

        //Create the right side with the Initial list and Updated list
        JPanel rightPane = new JPanel(); {
            //Create the Button # label and comboBox
            JPanel orderPanel = new JPanel(); {
                orderPanel.setLayout(new FlowLayout());

                Font font = new Font("Verdana", Font.BOLD,12);

                JLabel orderLabel = new JLabel("Button #: ");
                orderLabel.setFont(font);
                if(orderModel == null) {
                    orderModel = new DefaultComboBoxModel();
                    order = new JComboBox(orderModel);
                    for (int i = 1; i <= orderButtons.length; i++)
                        orderModel.addElement(i);
                }
                order.setBackground(Color.BLACK);
                order.setForeground(new Color(140, 171, 226));
                orderPanel.add(orderLabel);
                orderPanel.add(order);

                JLabel finalLabel = new JLabel("Add and remove Buttons: ");
                finalLabel.setFont(font);
                orderPanel.add(finalLabel);

                //Add Edit button to add audio files to final list.
                addFinalBtn = new JButton();
                ImageIcon addFinalIcn = new ImageIcon("Icons/Add.png");
                setButtonIcon(addFinalBtn, addFinalIcn);
                addFinalBtn.setToolTipText("Add to Final List");
                addFinalBtn.addActionListener(e -> addFinalBtn(audioFiles));
                addFinalBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(addFinalBtn);

                //Add remove button to new remove the last button created in simulator app.
                removeFinalBtn = new JButton();
                ImageIcon removeFinalIcn = new ImageIcon("Icons/Remove.png");
                setButtonIcon(removeFinalBtn, removeFinalIcn);
                removeFinalBtn.setToolTipText("Remove from Final List");
                removeFinalBtn.addActionListener(e -> removeFinalBtn());
                removeFinalBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(removeFinalBtn);

                //Add Record Button to record new audio files.
//                JButton recordBtn = new JButton();
//                ImageIcon recordIcn = new ImageIcon("Icons/Record.png");
//                setButtonIcon(recordBtn, recordIcn);
//                recordBtn.setToolTipText("Record");
//                recordBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                recordBtn.addActionListener(e -> {
//                    recordBtn.setVisible(false);
//                    ImageIcon stopRcdIcn = new ImageIcon("Icons/Stop.png");
//                    JButton stopRcdBtn = new JButton();
//                    setButtonIcon(stopRcdBtn, stopRcdIcn);
//                    stopRcdBtn.setToolTipText("Stop Recording");
//                    stopRcdBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                    stopRcdBtn.addActionListener(e1 -> {
//                        stopRcdBtn.setVisible(false);
//                        recordBtn.setVisible(true);
//                        stopRecording();
//                    });
//                    startRecording();
//                    orderPanel.add(stopRcdBtn);
//                });
//                orderPanel.add(recordBtn);
                // Add button to launch Simulator App
                launchSimApp = new JButton();
                ImageIcon launchIcn = new ImageIcon("Icons/Launch.png");
                setButtonIcon(launchSimApp, launchIcn);
                launchSimApp.setToolTipText("Launch Simulator");
                launchSimApp.addActionListener(e ->  {
                    if(sounds.exists()) {
                        myFrame = new SimulatorApp();
                        myFrame.setVisible(true);
                        myFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Please try to save before launching simulator app");
                    }
                });
                launchSimApp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(launchSimApp);
            }
            rightPane.setLayout(new BorderLayout(8, 8));
            rightPane.add(orderPanel, BorderLayout.CENTER);
//            initialListModel = new DefaultListModel();
//            initialList = new JList(initialListModel);
            // Create the scrolled list for Initial List
//            if(initialListModel == null)
//            {
//                initialListModel = new DefaultListModel();
//                initialList = new JList(initialListModel);
//                for (int i = 0; i < order.getItemCount(); i++)
//                    initialListModel.addElement(audioFiles[i]);
//            }
            setBackground(initialList);
            initialList.setPrototypeCellValue("XXXXXXXXXXXXXXXXXXX");
            leftScrollPane = new JScrollPane(initialList);
            leftScrollPane.setColumnHeaderView(new JLabel("What You Have Now"));
            rightPane.add(leftScrollPane, BorderLayout.WEST);
            if(sounds.exists())
                leftScrollPane.setVisible(true);
            else
                leftScrollPane.setVisible(false);

            //Create the scrolled list for Final List
            if(finalListModel == null)
            {
                finalListModel = new DefaultListModel();
                finalList = new JList(finalListModel);
                for (int i = 0; i < order.getItemCount(); i++)
                    finalListModel.addElement(audioFiles[i]);
            }
            setBackground(finalList);
            finalList.setPrototypeCellValue("XXXXXXXXXXXXXXXXXXXX");
            rightScrollPane = new JScrollPane(finalList);

            //Add all elements from initial list to final list when app is started.
//            for (int i=0; i<initialListModel.getSize(); i++)
//                finalListModel.addElement(initialListModel.elementAt(i));

            rightScrollPane.setColumnHeaderView(new JLabel("Changes you Want"));
            rightPane.add(rightScrollPane, BorderLayout.EAST);
            //rightPane.setBackground(Color.BLACK);
        }
        contentPane.add(rightPane, BorderLayout.SOUTH);

        // Create the center with image, text label, and slider
        JPanel centerPane = new JPanel(); {
            centerPane.setLayout(new BorderLayout(8, 8));

            JLabel image = new JLabel(new ImageIcon("title.jpg"));
            centerPane.add(image, BorderLayout.NORTH);
            centerPane.setBackground(Color.BLACK);

            infoLabel = new JLabel("  ");
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            infoLabel.setForeground(new Color(140,171,226));
            centerPane.add(infoLabel, BorderLayout.CENTER);

            slider = new JSlider(0, 100, 0);
            TitledBorder border = new TitledBorder("Seek");
            border.setTitleColor(Color.white);
            slider.setBorder(new CompoundBorder(new EmptyBorder(6, 10, 10, 10), border));
            slider.addChangeListener(this);
            slider.setBackground(Color.BLACK);
            slider.setMajorTickSpacing(25);
            slider.setPaintTicks(true);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    player.setVolume(slider.getValue());
                }
            });
            centerPane.add(slider, BorderLayout.SOUTH);
            //centerPane.setBackground(Color.BLACK);
        }
        contentPane.add(centerPane, BorderLayout.EAST);

        // Create the toolbar with the buttons
        JPanel toolbar = new JPanel(); {
            toolbar.setLayout(new GridLayout(1, 0));

            playBtn = new JButton();
            ImageIcon playIcn = new ImageIcon("Icons/Play.png");
            playBtn.setToolTipText("Play");
            setButtonIcon(playBtn, playIcn);
            playBtn.addActionListener(e -> play());
            playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(playBtn);

            stopBtn = new JButton();
            ImageIcon stopIcn = new ImageIcon("Icons/Stop.png");
            setButtonIcon(stopBtn, stopIcn);
            stopBtn.setToolTipText("Stop");
            stopBtn.addActionListener(e -> stop());
            stopBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(stopBtn);

            pauseBtn = new JButton();
            ImageIcon pauseIcn = new ImageIcon("Icons/Pause.png");
            setButtonIcon(pauseBtn, pauseIcn);
            pauseBtn.setToolTipText("Pause");
            pauseBtn.addActionListener(e -> pause());
            pauseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(pauseBtn);

            resumeBtn = new JButton();
            ImageIcon resumeIcn = new ImageIcon("Icons/Play.png");
            setButtonIcon(resumeBtn, resumeIcn);
            resumeBtn.setToolTipText("Resume");
            resumeBtn.addActionListener(e -> resume());
            resumeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(resumeBtn);

            resetBtn = new JButton();
            ImageIcon resetIcn = new ImageIcon("Icons/Reset.png");
            setButtonIcon(resetBtn, resetIcn);
            resetBtn.setToolTipText("Reset");
            resetBtn.addActionListener(e -> reset(audioFiles));
            resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(resetBtn);

            swapBtn = new JButton();
            ImageIcon swapIcn = new ImageIcon("Icons/Swap.png");
            setButtonIcon(swapBtn, swapIcn);
            swapBtn.setToolTipText("Swap");
            swapBtn.addActionListener(e -> swap(audioFiles));
            swapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(swapBtn);

            saveChangesBtn = new JButton();
            ImageIcon saveChangesIcn = new ImageIcon("Icons/Save.png");
            setButtonIcon(saveChangesBtn, saveChangesIcn);
            saveChangesBtn.setToolTipText("Save Changes");
            saveChangesBtn.addActionListener(e -> saveChanges());
            saveChangesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(saveChangesBtn);
            //toolbar.setBackground(Color.BLACK);
        }

        contentPane.add(toolbar, BorderLayout.NORTH);
        //contentPane.setBackground(Color.black);

        // building is done - arrange the components
        pack();

        // place this frame at the center of the screen and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width/2 - getWidth()/2, d.height/2 - getHeight()/2);
        setVisible(true);
        if(!sounds.exists()) {
            tutorial();
        }
    }

    //Create the main frame's menu bar.
    private void makeMenuBar() {
        final int SHORTCUT_MASK =
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu menu;
        JMenuItem item;

        // create the File menu
        menu = new JMenu("File");
        menubar.add(menu);

        item = new JMenuItem("Add a new Audio File");
        item.addActionListener(e -> {
            try {
                copyFileUsingStream(pickFile(), sounds);
            }
            catch (Exception el) {
                JOptionPane.showMessageDialog(null, el.getMessage());
            }});
        menu.add(item);

        item = new JMenuItem("Quit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        item.addActionListener(e -> quit());
        menu.add(item);

        // create the Help menu
        menu = new JMenu("Help");
        menubar.add(menu);

        item = new JMenuItem("Tutorial");
        item.addActionListener(e -> tutorial());
        menu.add(item);

        item = new JMenuItem("About TalkBox...");
        item.addActionListener(e -> showAbout());
        menu.add(item);
    }

    private File pickFile()
    {
        File source = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            source = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + source.getAbsolutePath());
        }
        return source;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }


    //Background for the lists that will be used in the GUI
    private void setBackground(JList list) {
        list.setForeground(new Color(140, 171, 226));
        list.setBackground(new Color(0, 0, 0));
        list.setSelectionBackground(new Color(87, 49, 134));
        list.setSelectionForeground(new Color(140, 171, 226));
    }

    //Added the edit button functionality to add audio files to final list.
    private void addFinalBtn(String[] audioFiles) {
        if(audioList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an audio from audio list");
        }
        else {
            int reply = JOptionPane.showConfirmDialog(null, "You can select the position " +
                            "from the button number menu if already selected, Select No." +
                            "\n Or you want to add it at the last if so, Select Yes.\nTo exit Select Cancel.",
                    "Add Button.", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            String filename = (String)audioList.getSelectedValue();
            if(orderModel.getSize() < audioFiles.length) {
                if (finalListModel.contains(filename))
                    JOptionPane.showMessageDialog(null, "Sorry the item already exists.");
                else {
                    if(finalListModel.size() >= orderButtons.length )
                        orderModel.addElement(order.getItemCount() + 1);
                    if (reply == JOptionPane.YES_OPTION)
                        finalListModel.addElement(filename);
                    else if(reply == JOptionPane.NO_OPTION){
                        if(order.getSelectedIndex() == -1)
                            JOptionPane.showMessageDialog(null, "Please make a select a Button Number.");
                        else
                            finalListModel.add(order.getSelectedIndex(), filename);
                    }
                }
            }
//            if (finalListModel.size() < order.getItemCount())
//                if(finalListModel.size() < order.getSelectedIndex())
//                    JOptionPane.showMessageDialog(null, "Sorry, try to pick the previous buttons first");
//                else {
//                    if (finalListModel.contains(filename))
//                        JOptionPane.showMessageDialog(null, "Sorry the item already exists");
//                    else
//                        finalListModel.add(order.getSelectedIndex(), filename);
//                }
            else
                JOptionPane.showMessageDialog(null, "Sorry you have exceeded the " +
                        "number of available buttons, please add a new button, then try.");
        }
    }

    private void removeFinalBtn()
    {
        if (finalListModel.size() <= 0)
            JOptionPane.showMessageDialog(null, "Cannot remove more buttons.");
        else
            if(finalListModel.size() <= order.getSelectedIndex())
                JOptionPane.showMessageDialog(null, "Sorry, try to pick the previous buttons first.");
            else {
                finalListModel.remove(order.getSelectedIndex());
                if(finalListModel.size() >= orderButtons.length )
                    orderModel.removeElementAt(order.getItemCount() - 1);
            }
    }

    //Added the swap button functionality to swap existing set of audio files with the next set.
    public void swap(String[] audioFiles) {
        int index;
        int j = audioListModel.indexOf(audioListModel.lastElement());

        if(finalListModel.isEmpty())
            index = -1;
        else
            index = audioListModel.indexOf(finalListModel.lastElement());

        int length = order.getItemCount();
        //initialListModel.removeAllElements();
        finalListModel.removeAllElements();
        for(int i = 1; i <= length; i++) {
            int c = ++index;
            if(c > j) {
                c = 0;
                index = 0;
                finalListModel.addElement(audioFiles[c]);
            }
            else {
                finalListModel.addElement(audioFiles[c]);
            }
        }
    }

    //Added the reset button functionality to reset the initial list and final list
    public void reset(String[] audioFiles) {
        finalListModel.removeAllElements();
        //initialListModel.removeAllElements();
        order.removeAllItems();
        for(int i = 1; i <= orderButtons.length; i++) {
            //initialListModel.addElement(audioFiles[i - 1]);
            finalListModel.addElement(audioFiles[i - 1]);
            orderModel.addElement(i);
        }
    }

    //Added functionality to save changes.
    public void saveChanges() {
        if(finalListModel.isEmpty() || finalListModel.size() < orderButtons.length) {
            JOptionPane.showMessageDialog(null, "The list cannot be empty, there should " +
                    "be at least " + orderButtons.length + " buttons.");
        }
        else if((finalListModel.toString()).equals(initialListModel.toString())) {
            JOptionPane.showMessageDialog(null, "Sorry, make different selections as " +
                    "both final and initial list are same.");
        }
        else {
            save();
            initialListModel.removeAllElements();
            for (int i = 0; i < finalListModel.size(); i++) {
                initialListModel.addElement(finalListModel.getElementAt(i));
            }
            leftScrollPane.setVisible(true);
        }
    }

    public void save()
    {
        //Serialization
        SaveData data = new SaveData();
        data.finalList = new JList(finalListModel);
        data.order = new JComboBox(orderModel);
        data.numberOfAudioButtons = this.getNumberOfAudioButtons();
        System.out.println(data.numberOfAudioButtons);
        data.numberOfAudioSets = this.getNumberOfAudioSets();
        System.out.println(data.numberOfAudioSets);
        data.totalNumberOfButtons = this.getTotalNumberOfButtons();
        System.out.println(data.totalNumberOfButtons);
        c = 0;
        data.relativePathToAudioFiles = this.getRelativePathToAudioFiles().toString();
        System.out.println(data.relativePathToAudioFiles);
        data.audioFileNames = this.getAudioFileNames();
        System.out.println(Arrays.deepToString(data.audioFileNames).replace("], ", "]\n"));
        try {
            ResourceManager.save(data, fileName);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private  void tutorial()
    {
        JOptionPane.showMessageDialog(null, "The Buttons on the top row, can be used" +
                " to\n1. Play, 2. Stop, 3. Pause, 4. Resume audio files, \n5. Reset the configuration, 6. Swap audio files" +
                " and 7. Save Changes."
                , "Tutorial: Top Row", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The 'all formats' drop down menu can be used to display audio " +
                "files of a specific format in the Audio List."
                , "Tutorial: Select Audio Format", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The  'Audio Files' List shows all the available " +
                "audio files.\nYou will choose files from here to test play them and add them to Simulator App."
                , "Tutorial: Audio List", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The window on the top right shows the name " +
                "and duration of the audio file you have chosen to play "
                , "Tutorial: Right Window", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The List on the left (What You Have) will display the " +
                "current settings\n such as the number of buttons and what audio files are assigned to them in Simulator " +
                "App.\nYou will have to save some configurations first using the save button to see this List."
                , "Tutorial: List On The Left", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The List on the bottom right (Changes You Want) " +
                "will display the changes you have made,\nas soon as you make them, if you are satisfied with the new settings," +
                "\nyou can save them using save button in the top row."
                , "Tutorial: List On The Right", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The '+' and 'X' buttons can be used to add or remove " +
                "audio file buttons from the Simulator App."
                , "Tutorial: '+' and 'X' Buttons", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "You can select Button Number from Button# drop down " +
                "to add or remove an audio file on a specific position."
                ,"Tutorial: Button #", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "The Rocket is used to Shuttle launch the " +
                "Simulator App, Do remember to save some configurations before launching it."
                , "Tutorial: Launch Simulator App", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "For more info about buttons, just keep " +
                "the cursor on the button for 3-5 seconds."
                , "Tutorial: Info On Buttons", JOptionPane.INFORMATION_MESSAGE);
    }

    //Method to start recording
    private void startRecording() {

    }

    //Method to stop recording
    private void stopRecording() {

    }

    //Method to set the Button Icon
    private void setButtonIcon(JButton button, ImageIcon myIcon1) {
        //button.setBackground(Color.black);
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setFocusable(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setIcon(myIcon1);
        button.setOpaque(false);
    }

    @Override
    public int getNumberOfAudioButtons() {
        int audioButtonCount = order.getItemCount();
        //System.out.println(audioButtonCount);
        return audioButtonCount;
    }

    @Override
    public int getNumberOfAudioSets() {
        int audioSetsCount = audioListModel.size() / order.getItemCount();
        if(audioListModel.size() % this.getNumberOfAudioButtons() != 0)
            audioSetsCount++;
        //System.out.println(audioSetsCount);
        return audioSetsCount;
    }

    @Override
    public int getTotalNumberOfButtons() {
        if (comp == null) {
            comp = getContentPane().getComponents();
        }
        for (Component container : comp) {
            if (container instanceof JButton)
                c++;
            else if (container instanceof JPanel) {
                comp = ((JPanel) container).getComponents();
                getTotalNumberOfButtons();
            }
        }
        comp = null;
        System.out.println("c is: " + c);
        return c;
    }

    @Override
    public Path getRelativePathToAudioFiles() {
        Path path1 = Paths.get(".\\Sounds");
        //System.out.println(path1);
        return path1;
    }

    @Override
    public String[][] getAudioFileNames() {
        int rows = this.getNumberOfAudioSets();
        int columns = this.getNumberOfAudioButtons();
        String[][] audioFileNames= new String[rows][columns];
        int counter = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (counter == audioListModel.size()) {
                        counter = 0;
                        audioFileNames[i][j] = (String) audioList.getModel().getElementAt(counter);
                        counter++;
                    } else {
                        audioFileNames[i][j] = (String) audioList.getModel().getElementAt(counter);
                        counter++;
                    }
                }
            }
        //System.out.println(Arrays.deepToString(audioFileNames).replace("], ", "]\n"));
        return audioFileNames;
    }
}

//After orderModel is added.
//                JLabel initialLabel = new JLabel("Initial List: ");
//                initialLabel.setFont(font);
//                orderPanel.add(initialLabel);
//
//                //Add button to add new button in simulator app.
//                addNewBtn = new JButton();
//                ImageIcon addNewIcn = new ImageIcon("Icons/Add.png");
//                setButtonIcon(addNewBtn, addNewIcn);
//                addNewBtn.setToolTipText("Add New Button");
//                addNewBtn.addActionListener(e -> addInitialBtn(audioFiles));
//                addNewBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                orderPanel.add(addNewBtn);
//
//                //Add remove button to new remove the last button created in simulator app.
//                removeNewBtn = new JButton();
//                ImageIcon removeIcn = new ImageIcon("Icons/Remove.png");
//                setButtonIcon(removeNewBtn, removeIcn);
//                removeNewBtn.setToolTipText("Remove from Initial List");
//                removeNewBtn.addActionListener(e -> removeInitialBtn());
//                removeNewBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                orderPanel.add(removeNewBtn);
//                  orderPanel.setBackground(Color.BLACK);
//Added functionality to add button to add new button to simulator app.
//    private void addInitialBtn(String[] audioFiles) {
//        int index = audioListModel.indexOf(initialListModel.lastElement());
//        if(orderModel.getSize() < audioFiles.length) {
//            orderModel.addElement(order.getItemCount() + 1);
//            if(++index == audioListModel.size()) {
//                index = 0;
//                initialListModel.addElement(audioFiles[index++]);
//            }
//            else
//                initialListModel.addElement(audioFiles[index++]);
//        }
//        else
//            JOptionPane.showMessageDialog(null, "Sorry you can't add more buttons");
//    }

//Added functionality to remove button to remove the last created button in simulator app.
//    private void removeInitialBtn() {
//        if(order.getItemCount() > orderButtons.length) {
//            if(finalListModel.size() == initialListModel.size() || finalListModel.size() >= initialListModel.size()) {
//                finalListModel.removeElementAt(order.getItemCount() - 1);
//                orderModel.removeElementAt(order.getItemCount() - 1);
//                initialListModel.removeElementAt(order.getItemCount());
//            }
//            else {
//                orderModel.removeElementAt(order.getItemCount() - 1);
//                initialListModel.removeElementAt(order.getItemCount());
//            }
//        }
//        else
//            JOptionPane.showMessageDialog(null, "Sorry, you can't remove more buttons");
//    }
