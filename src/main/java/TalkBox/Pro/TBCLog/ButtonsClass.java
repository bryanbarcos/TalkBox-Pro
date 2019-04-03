package main.java.TalkBox.Pro.TBCLog;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsClass extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JButton today;
	public JButton yesterday;
	public JButton pastMonth;

	public ButtonsClass() {
		createButtons();
	}
	
	public void createButtons() {
		today = new JButton("Today");
		yesterday = new JButton("Yesterday");
		pastMonth = new JButton("Past Month");
		
		this.add(today);
		this.add(yesterday);
		this.add(pastMonth);
	}

}
