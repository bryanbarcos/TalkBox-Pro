package main.java.TalkBox.Pro.TBCLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class TextReader extends JTextArea{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TextReader() {
		this.setSize(WIDTH, HEIGHT);
		this.setEditable(false);
		this.readAndPrintToGUI();
	}
	
	public void readAndPrintToGUI() {
		
		String text;
		
		try (BufferedReader br = new BufferedReader(new FileReader("Logger.txt"))) {
			while ((text = br.readLine()) != null) {
				this.append(text + "\n");
			}
			
		}
		
		catch (IOException e) {
			System.out.println("File not found");
		}
		
	}
	
}
