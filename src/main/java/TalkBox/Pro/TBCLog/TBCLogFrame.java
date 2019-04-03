package main.java.TalkBox.Pro.TBCLog;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class TBCLogFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	
	private String title;
	public final int WIDTH = 700; 
	public final int HEIGTH = 700;
	
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
		frame.setMaximumSize(new Dimension(WIDTH, HEIGTH));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		this.createScrollPanel();
		frame.setVisible(true);
	}
	
	private void createScrollPanel() {
		scrollPane = new ScrollPaneClass();
		scrollPane.setVisible(true);
		frame.add(scrollPane);
	}
	
	public static void main(String args[]) {
		new TBCLogFrame();
	}
}
