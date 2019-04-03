package main.java.TalkBox.Pro.TBCLog;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollPaneClass extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private TextReader textReader;
	
	public ScrollPaneClass() {
		this.setLayout(new BorderLayout());
		createScrollPane();
		this.add(scrollPane);
	}
	
	private void createScrollPane() {
		textReader = new TextReader();
		scrollPane = new JScrollPane(textReader);
	}

}
