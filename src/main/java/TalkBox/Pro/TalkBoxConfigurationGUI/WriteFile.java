package main.java.TalkBox.Pro.TalkBoxConfigurationGUI;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WriteFile {

	private String path;
	private boolean writeToFile = false;
	
	public WriteFile(String path) {
		this.path = path;
	}
	
	public WriteFile(String path, boolean writeToFile) {
		this.writeToFile = writeToFile;
		this.path = path;
	}
	
	public void writeToFile(String textLine) throws IOException {
		
		FileWriter write = new FileWriter(path, writeToFile);
		PrintWriter printLine = new PrintWriter(write);
		printLine.printf("%s" + "%n", textLine);
		printLine.close();
	}
}
