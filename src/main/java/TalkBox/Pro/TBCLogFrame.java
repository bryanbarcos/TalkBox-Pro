package main.java.TalkBox.Pro;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TBCLogFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3023175267144877950L;
	/**
	 * This GUI will display the Configuration app logs.
	 * This includes what buttons they press and what audio 
	 * files the user selects. 
	 * */
	
	private JFrame frame;
	public JPanel mainPanel;
	
	private String title;
	private int width;
	private int height;
	
	public TBCLogFrame(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		this.createFrame();
	}
	
	private void createFrame() {
		frame = new JFrame(title);
		frame.setLayout(new BorderLayout());
		frame.setSize(width, height);
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		frame.add(mainPanel);
		mainPanel.setVisible(true);
		frame.pack();
	}

}
