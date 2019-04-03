package main.java.TalkBox.Pro.TBCLog;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogCanvas extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea textArea;
	JScrollPane scrollPane;
	
	public LogCanvas() {
		textArea = new JTextArea(WIDTH,HEIGHT);
		scrollPane = new JScrollPane(textArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(textArea);
		this.setSize(WIDTH,HEIGHT);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
