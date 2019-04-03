package main.java.TalkBox.Pro.TBCLog;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class TBCLogFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	
	private String title;
	public final int WIDTH = 500; 
	public final int HEIGTH = 500;
	
	//private ButtonsClass btns;
	private ScrollPaneClass scrollPane;
	
	public TBCLogFrame() {
		this.title = "TBCLog Application";
		this.createWindow();
	}
	
	private void createWindow() {
		frame = new JFrame(title);
		frame.setLayout(new BorderLayout());
		frame.setSize(WIDTH, HEIGTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		this.createScrollPanel();
		frame.setVisible(true);
		
		
		/*this.createButtonsPanel();*/
		//this.createScrollPanel();
	}
	
	/*private void createButtonsPanel() {
		btns = new ButtonsClass();
		btns.setVisible(true);
		frame.add(btns, BorderLayout.NORTH);
	}*/
	
	private void createScrollPanel() {
		scrollPane = new ScrollPaneClass();
		scrollPane.setVisible(true);
		frame.add(scrollPane, BorderLayout.CENTER);
	}
	
	//private void create
}
