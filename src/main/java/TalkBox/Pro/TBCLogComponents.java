package main.java.TalkBox.Pro;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TBCLogComponents {
	
	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;
	
	private TBCLogFrame frame;
	private JPanel timePanel;
	private JPanel logHistory;
	
	private JButton todayButton;
	private JButton yesterdayButton;
	private JButton pastWeekButton;
	private JButton lastMonthButton;
	
	public TBCLogComponents() {
		frame = new TBCLogFrame("TBCLog", HEIGHT, WIDTH);
		this.addtimePanel();
	}
	
	public void addtimePanel() {
		todayButton = new JButton("Today");
		yesterdayButton = new JButton("Yesterday");
		pastWeekButton = new JButton("Past Week");
		lastMonthButton = new JButton("Last Month");
		
		timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
		timePanel.add(todayButton);
		timePanel.add(yesterdayButton);
		timePanel.add(pastWeekButton);
		timePanel.add(lastMonthButton);
		
		frame.mainPanel.add(timePanel, BorderLayout.NORTH);
	}
}
