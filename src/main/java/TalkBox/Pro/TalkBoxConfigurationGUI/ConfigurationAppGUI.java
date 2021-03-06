package main.java.TalkBox.Pro.TalkBoxConfigurationGUI;

import main.java.TalkBox.Pro.TBCLog.TBCLogFrame;
import main.java.TalkBox.Pro.simulatorGUI.SimulatorApp;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private static final String AUDIO_DIR = Paths.get("Sounds").toString();
    private static final String fileName = "TalkBoxConfig.txt";
    

    
    public JList audioList;
    private JSlider slider;
    private JLabel infoLabel;
    public SoundEngine player;
    public JList initialList;
    public JList finalList;
    private JComboBox <Integer> order;
    public DefaultListModel initialListModel;
    public DefaultListModel finalListModel;
    public DefaultListModel audioListModel;
    public Integer[] orderButtons = {1, 2, 3};
    private DefaultComboBoxModel orderModel;
    Component[] comp;
    int c = 0;
    
    public int reply;
    
    public final int YES = JOptionPane.YES_OPTION;
	public final int NO = JOptionPane.NO_OPTION;
	public int yesOrNo;

    public JButton playBtn;
    public JButton stopBtn;
    public JButton pauseBtn;
    public JButton resumeBtn;
    public JButton resetBtn;
    public JButton swapBtn;
    public JButton saveChangesBtn;
    public JButton addFinalBtn;
    public JButton addNewBtn;
    public JButton removeNewBtn;
    public JButton removeFinalBtn;
    public JButton launchSimApp;
    public JButton launchLogApp;
    JScrollPane rightScrollPane;
    JScrollPane leftScrollPane;
    public SimulatorApp myFrame;
    public TBCLogFrame logApp;
    File savedConfig = new File(fileName);
    File sounds = new File(AUDIO_DIR);


    //Main method for starting the player from a command line.
    public static void main(String[] args){ ConfigurationAppGUI gui = new ConfigurationAppGUI(); }
    
    public static boolean isJUnitTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }           
        }
        return false;
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
            if(sounds.exists() && sounds.isDirectory() && sounds.list().length > 0) {
                if (savedConfig.exists()) {
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
            }
            
            
            
          //DOUBLE CHECK THIS FOR TESTING
            
            else {
                JOptionPane.showMessageDialog(null, "Sorry the Sounds directory does not exist" +
                        " or it is not a directory or it is empty.\n" +
                        "please fix it by creating the directory and copy at least one '.wav' file in the directory " +
                        "before opening this App.");
                System.exit(0);
            }

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
        	if (isJUnitTest() == false) {
            JOptionPane.showMessageDialog(null, "Please select a file from Audio Files to play.");
            return;
        	}
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

    public static String[] findFiles(String dirName, String suffix) {
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
        	if (isJUnitTest() == false) {
        		JOptionPane.showMessageDialog(null, "Error: " + dirName + " must be a directory");
        		return null;
        		}
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

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(new EmptyBorder(6, 10, 10, 10));
        contentPane.setPreferredSize(new Dimension(800, 500));
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
                addFinalBtn.addActionListener(e -> 
                { 
                	addFinalBtn(audioFiles);
                	WriteFile data = new WriteFile("Logger.txt", true);
                    try {
                    	Date now = new Date();
            			data.writeToFile(now + ": The 'Add Audio File Button' for the Final Audio List was pressed");
            		} catch (IOException j) {
            			// TODO Auto-generated catch block
            			j.printStackTrace();
            		}
                });
                addFinalBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(addFinalBtn);

                //Add remove button to new remove the last button created in simulator app.
                removeFinalBtn = new JButton();
                ImageIcon removeFinalIcn = new ImageIcon("Icons/Remove.png");
                setButtonIcon(removeFinalBtn, removeFinalIcn);
                removeFinalBtn.setToolTipText("Remove from Final List");
                removeFinalBtn.addActionListener(e ->
                { 
                	removeFinalBtn();
                	WriteFile data = new WriteFile("Logger.txt", true);
                    try {
                    	Date now = new Date();
            			data.writeToFile(now + ": The 'Remove Audio File Button' for the Final Audio List was pressed");
            		} catch (IOException j) {
            			// TODO Auto-generated catch block
            			j.printStackTrace();
            		}
                });
                removeFinalBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(removeFinalBtn);


                // Add button to launch Simulator App
                launchSimApp = new JButton();
                ImageIcon launchIcn = new ImageIcon("Icons/Launch.png");
                setButtonIcon(launchSimApp, launchIcn);
                launchSimApp.setToolTipText("Launch Simulator");
                launchSimApp.addActionListener(e ->  {
                    if(savedConfig.exists()) {
                        myFrame = new SimulatorApp();
                        myFrame.setVisible(true);
                        myFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    }
                    else {
                    	if (isJUnitTest() == false) {
                    		JOptionPane.showMessageDialog(null, "Please try to save before launching simulator app");
                    	}  
                    }
                    WriteFile data = new WriteFile("Logger.txt", true);
                    try {
                    	Date now = new Date();
            			data.writeToFile(now + ": The 'Simulator App' was pressed");
            		} catch (IOException j) {
            			// TODO Auto-generated catch block
            			j.printStackTrace();
            		}
                });
                launchSimApp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(launchSimApp);
                
                
                launchLogApp = new JButton("TBCLogApp");
                launchLogApp.setToolTipText("TBC Log Application");
                launchLogApp.addActionListener(e ->{
                	logApp = new TBCLogFrame();
                	logApp.setVisible(true);
                	logApp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                });
                launchLogApp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                orderPanel.add(launchLogApp);
            }
            rightPane.setLayout(new BorderLayout(8, 8));
            rightPane.add(orderPanel, BorderLayout.CENTER);

            setBackground(initialList);
            initialList.setPrototypeCellValue("XXXXXXXXXXXXXXXXXXX");
            leftScrollPane = new JScrollPane(initialList);
            leftScrollPane.setColumnHeaderView(new JLabel("What You Have Now"));
            rightPane.add(leftScrollPane, BorderLayout.WEST);
            if(savedConfig.exists())
                leftScrollPane.setVisible(true);
            else
                leftScrollPane.setVisible(false);

            //Create the scrolled list for Final List
            if(finalListModel == null) {
                finalListModel = new DefaultListModel();
                finalList = new JList(finalListModel);
                for (int i = 0; i < order.getItemCount(); i++) {
                    System.out.println( i+1+". " +audioFiles[i]);
                    finalListModel.addElement(audioFiles[i]);
                }
            }
            setBackground(finalList);
            finalList.setPrototypeCellValue("XXXXXXXXXXXXXXXXXXXX");
            rightScrollPane = new JScrollPane(finalList);


            rightScrollPane.setColumnHeaderView(new JLabel("Changes you Want"));
            rightPane.add(rightScrollPane, BorderLayout.EAST);
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
            centerPane.add(slider, BorderLayout.SOUTH);
            centerPane.setBackground(Color.BLACK);
        }
        contentPane.add(centerPane, BorderLayout.EAST);

        // Create the toolbar with the buttons
        JPanel toolbar = new JPanel(); {
            toolbar.setLayout(new GridLayout(1, 0));

            playBtn = new JButton();
            ImageIcon playIcn = new ImageIcon("Icons/Play.png");
            playBtn.setToolTipText("Play");
            setButtonIcon(playBtn, playIcn);
            playBtn.addActionListener(e -> {
                play();
                WriteFile data = new WriteFile("Logger.txt", true);
                try {
                	Date now = new Date();
        			data.writeToFile(now + ": The 'Play Audio File' was pressed");
        		} catch (IOException j) {
        			// TODO Auto-generated catch block
        			j.printStackTrace();
        		}
            });
            playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(playBtn);

            final Boolean[] isPaused = {false};
            pauseBtn = new JButton();
            ImageIcon pauseIcn = new ImageIcon("Icons/Pause.png");
            setButtonIcon(pauseBtn, pauseIcn);
            pauseBtn.setToolTipText("Pause");
            pauseBtn.addActionListener(e -> {
                if(!isPaused[0]) {
                    ImageIcon resumeIcn = new ImageIcon("Icons/Play.png");
                    setButtonIcon(pauseBtn, resumeIcn);
                    pauseBtn.setToolTipText("Resume");
                    pause();
                    
                    WriteFile data = new WriteFile("Logger.txt", true);
                    try {
                    	Date now = new Date();
            			data.writeToFile(now + ": The 'Pause Button' was pressed");
            		} catch (IOException j) {
            			// TODO Auto-generated catch block
            			j.printStackTrace();
            		}
                }
                else {
                    setButtonIcon(pauseBtn, pauseIcn);
                    pauseBtn.setToolTipText("Pause");
                    resume();
                    
                    WriteFile data = new WriteFile("Logger.txt", true);
                    try {
                    	Date now = new Date();
            			data.writeToFile(now + ": The 'Resume Button' was pressed");
            		} catch (IOException j) {
            			// TODO Auto-generated catch block
            			j.printStackTrace();
            		}
                }
                isPaused[0] = !isPaused[0];
                
                
                });
            pauseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(pauseBtn);

            stopBtn = new JButton();
            ImageIcon stopIcn = new ImageIcon("Icons/Stop.png");
            setButtonIcon(stopBtn, stopIcn);
            stopBtn.setToolTipText("Stop");
            stopBtn.addActionListener(e -> 
            {
            	stop();
            	
            	WriteFile data = new WriteFile("Logger.txt", true);
                try {
                	Date now = new Date();
        			data.writeToFile(now + ": The 'Stop Button' was pressed");
        		} catch (IOException j) {
        			// TODO Auto-generated catch block
        			j.printStackTrace();
        		}
            });
            stopBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(stopBtn);

            resetBtn = new JButton();
            ImageIcon resetIcn = new ImageIcon("Icons/Reset.png");
            setButtonIcon(resetBtn, resetIcn);
            resetBtn.setToolTipText("Reset");
            resetBtn.addActionListener(e -> 
            {
            	reset(audioFiles);
            	
            	WriteFile data = new WriteFile("Logger.txt", true);
                try {
                	Date now = new Date();
        			data.writeToFile(now + ": The 'Reset Button' was pressed");
        		} catch (IOException j) {
        			// TODO Auto-generated catch block
        			j.printStackTrace();
        		}
            });
            resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(resetBtn);

            swapBtn = new JButton();
            ImageIcon swapIcn = new ImageIcon("Icons/Swap.png");
            setButtonIcon(swapBtn, swapIcn);
            swapBtn.setToolTipText("Swap");
            swapBtn.addActionListener(e -> 
            {
            	swap(audioFiles);
            	WriteFile data = new WriteFile("Logger.txt", true);
                try {
                	Date now = new Date();
        			data.writeToFile(now + ": The 'Swap Button' was pressed");
        		} catch (IOException j) {
        			// TODO Auto-generated catch block
        			j.printStackTrace();
        		}
            });
            swapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(swapBtn);

            saveChangesBtn = new JButton();
            ImageIcon saveChangesIcn = new ImageIcon("Icons/Save.png");
            setButtonIcon(saveChangesBtn, saveChangesIcn);
            saveChangesBtn.setToolTipText("Save Changes");
            saveChangesBtn.addActionListener(e -> 
            {
            	saveChanges();
            	
            	WriteFile data = new WriteFile("Logger.txt", true);
                try {
                	Date now = new Date();
        			data.writeToFile(now + ": The 'Save Changes Button' was pressed");
        		} catch (IOException j) {
        			// TODO Auto-generated catch block
        			j.printStackTrace();
        		}
            });
            saveChangesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toolbar.add(saveChangesBtn);
        }

        contentPane.add(toolbar, BorderLayout.NORTH);

        // building is done - arrange the components
        pack();

        // place this frame at the center of the screen and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width/2 - getWidth()/2, d.height/2 - getHeight()/2);
        setVisible(true);
        if(!savedConfig.exists() && isJUnitTest() == false) {
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
                File source = pickFile();
                copyFileUsingStream(source, new File(AUDIO_DIR + "/" + source.getName()));
                audioListModel.addElement(source.getName());
            }
            catch (Exception el) {
                JOptionPane.showMessageDialog(null, el.getMessage());
            }
            
            WriteFile data = new WriteFile("Logger.txt", true);
            try {
            	Date now = new Date();
    			data.writeToFile(now + ": The 'Add New Audio File' from top menu was pressed");
    		} catch (IOException j) {
    			// TODO Auto-generated catch block
    			j.printStackTrace();
    		}
            });
        menu.add(item);

        item = new JMenuItem("Quit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        item.addActionListener(e -> quit());
        menu.add(item);

        // create the Help menu
        menu = new JMenu("Help");
        menubar.add(menu);

        item = new JMenuItem("Tutorial");
        item.addActionListener(e -> 
        {
        	tutorial();
        	
        	WriteFile data = new WriteFile("Logger.txt", true);
            try {
            	Date now = new Date();
    			data.writeToFile(now + ": The 'Tutorial' from top menu was pressed");
    		} catch (IOException j) {
    			// TODO Auto-generated catch block
    			j.printStackTrace();
    		}
        });
        menu.add(item);

        item = new JMenuItem("About TalkBox...");
        item.addActionListener(e -> 
        {
        	showAbout();
        	
        	WriteFile data = new WriteFile("Logger.txt", true);
            try {
            	Date now = new Date();
    			data.writeToFile(now + ": The 'About TalkBox' from menu was pressed");
    		} catch (IOException j) {
    			// TODO Auto-generated catch block
    			j.printStackTrace();
    		}
        });
        menu.add(item);
    }

    private File pickFile() {
        File source = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files (.wav, .au, .aif)",
                "wav", "au", "aif");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            source = fileChooser.getSelectedFile();
        }
        return source;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        if(!dest.exists())
            Files.copy(source.toPath(), dest.toPath());
        else
            JOptionPane.showMessageDialog(null, "The File already exists");
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
        	if (isJUnitTest() == false) {
        		JOptionPane.showMessageDialog(null, "Please select an audio from audio list");
        	}
        }
        else {
        	if (isJUnitTest() == true) {
        		if (yesOrNo == YES) {
        			reply = YES;
        		}
        		else {
        			reply = NO;
        		}
        	}
        	if (isJUnitTest() == false) {
            reply = JOptionPane.showConfirmDialog(null, "1. To add to the last " +
                            "position.(Select Yes)\n2. To add to the selected button number from it's drop down " +
                            "menu if you have selected.(Select No)\n3.To Exit.(Select Cancel)",
                    "Add Button.", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        	}
            String filename = (String)audioList.getSelectedValue();
            if(orderModel.getSize() < audioFiles.length) {
                if (finalListModel.contains(filename)) {
                	if (isJUnitTest() == false) {
                		JOptionPane.showMessageDialog(null, "Sorry the item already exists.");
                	}
                }
                else {
                    if(finalListModel.size() >= orderButtons.length)
                        orderModel.addElement(order.getItemCount() + 1);
                    if (reply == JOptionPane.YES_OPTION)
                        finalListModel.addElement(filename);
                    else if(reply == JOptionPane.NO_OPTION){
                        if(order.getSelectedIndex() == -1)
                            JOptionPane.showMessageDialog(null, "Please select a Button Number.");
                        else
                            finalListModel.add(order.getSelectedIndex(),filename);
                    }
                }
            }

            else
                JOptionPane.showMessageDialog(null, "Sorry you have exceeded the " +
                        "number of available buttons, please add a new button, then try.");
        }
    }

    

    private void removeFinalBtn() {
        if (finalListModel.size() <= 0) {
        	if (isJUnitTest() == false) {
            	JOptionPane.showMessageDialog(null, "Cannot remove more buttons.");
        	}
        }
        else
            if(finalListModel.size() <= order.getSelectedIndex()) {
                JOptionPane.showMessageDialog(null, "Sorry, try to pick the previous buttons first.");
            }
            else {
                finalListModel.remove(order.getSelectedIndex());
                if(finalListModel.size() >= orderButtons.length ) {
                    orderModel.removeElementAt(order.getItemCount() - 1);
                }
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
        if (initialListModel.size() > 0) {
        	finalListModel.removeAllElements();
        	order.removeAllItems();
        	for(int i = 0; i < initialListModel.getSize(); i++) {
        		finalListModel.addElement(initialListModel.getElementAt(i));
        		order.addItem(i + 1);
        	}
        }
    }

    //Added functionality to save changes.
    public void saveChanges() {
        if((finalListModel.isEmpty() || finalListModel.size() < orderButtons.length) && isJUnitTest() == false) {
            JOptionPane.showMessageDialog(null, "The list cannot be empty, there should " +
                    "be at least " + orderButtons.length + " buttons.");
        }
        else if((finalListModel.toString()).equals(initialListModel.toString()) && isJUnitTest() == false) {
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
            if (isJUnitTest() == false) {
            	JOptionPane.showMessageDialog(null, "Great!!!\nYour Changes have been made in " +
                    "Simulator App. \nEnjoy!");
            }
        }
    }

    public void save() {
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
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void tutorial()
    {
        String[] buttons = { "OK", "SKIP"};
        String[] tutorialText = {"Welcome to TalkBox Configuration App!, A short tutorial for you" +
                        ", you can skip it if you want."
                , "The Buttons on the top row, can be used" +
                " to\n1. Play, 2. Stop, 3. Pause, 4. Resume audio files, \n5. Reset the configuration, 6. Swap audio files" +
                " and 7. Save Changes."
                , "The 'all formats' drop down menu can be used to display audio " +
                "files of a specific format in the Audio List."
                , "The  'Audio Files' List shows all the available " +
                "audio files.\nYou will choose files from here to test play them and add them to Simulator App."
                , "The window on the top right shows the name " +
                "and duration of the audio file you have chosen to play.\nYou can use the 'Seek' slider to fast-forward or " +
                "rewind the audio file which is playing."
                , "The List on the left (What You Have) will display the " +
                "current settings\n such as the number of buttons and what audio files are assigned to them in Simulator " +
                "App.\nYou will have to save some configurations first using the save button to see this List."
                , "The List on the bottom right (Changes You Want) " +
                "will display the changes you have made,\nas soon as you make them, if you are satisfied with the new settings," +
                "\nyou can save them using save button in the top row."
                , "The '+' and 'X' buttons can be used to add or remove " +
                "audio file buttons from the Simulator App."
                , "You can select Button Number from Button# drop down " +
                "to add or remove an audio file on a specific position."
                , "The Rocket is used to Shuttle launch the " +
                "Simulator App, Do remember to save some configurations before launching it."
                , "For more info about buttons, just keep " +
                "the cursor on the button for 3-5 seconds."
        };
    	

        String[] tutorialTitle = {"Welcome To TalkBox"
                , "Tutorial: Top Row"
                , "Tutorial: Select Audio Format"
                , "Tutorial: Audio List"
                , "Tutorial: Right Window"
                , "Tutorial: List On The Left"
                , "Tutorial: List On The Right"
                , "Tutorial: '+' and 'X' Buttons"
                , "Tutorial: Button #"
                , "Tutorial: Launch Simulator App"
                , "Tutorial: Info On Buttons"
        };
        int reply = JOptionPane.showOptionDialog(null, tutorialText[0], tutorialTitle[0]
                , JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
        for(int i = 1; i < tutorialText.length; i++) {
            if(reply == JOptionPane.YES_OPTION) {
                reply = JOptionPane.showOptionDialog(null, tutorialText[i], tutorialTitle[i]
                        , JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
            }
            else {
                JOptionPane.getRootFrame().dispose();
            }
        }
    }
    	
    //Method to set the Button Icon
    private void setButtonIcon(JButton button, ImageIcon myIcon1) {
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
        return audioButtonCount;
    }

    @Override
    public int getNumberOfAudioSets() {
        int audioSetsCount = audioListModel.size() / order.getItemCount();
        if(audioListModel.size() % this.getNumberOfAudioButtons() != 0)
            audioSetsCount++;
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
        return audioFileNames;
    }
}


