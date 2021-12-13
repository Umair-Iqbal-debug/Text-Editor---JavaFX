package utils;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.stage.FileChooser;

public class FileIO {

	private FileIO() {
	}

	private static final String projectPath = Path.getProjectPath();
	private static final String DEFAULT_FILENAME = "";
	
	
	public static String getFileName(String fullPath) {
		if(fullPath == null) return "Not saved yet";
		String[] temp = fullPath.split("\\\\");
		return temp[temp.length - 1];
	}

	public static String displayTextFileSaver(String contents, FileChooser fileChooser) {

		fileChooser.setInitialFileName(DEFAULT_FILENAME);

		File selectedFile = fileChooser.showSaveDialog(null);

		if (selectedFile != null && contents!=null) {
			saveFile(contents, selectedFile);
			return selectedFile.getAbsolutePath();
		}

		return null;
	}

	public static void saveFile(String content, File file) {

		// MAKE SURE USER ENTERS VALID EXTENSION .TXT

		if (file == null)
			return;

		try {
			FileWriter fw = new FileWriter(file);
			if(content == null) content = " ";
			fw.write(content);
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printContentOfDirectory() {

		File folder = new File("data");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
	}

	// relative path will be relative to the root folder of the project
	public static void write(String fileName, String relativePath, String contents) {
		String filePath = projectPath + relativePath + "/" + fileName;

		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
			PrintWriter pw = new PrintWriter(bos);

			for (int i = 0; i < contents.length(); i++) {
				pw.append(contents.charAt(i));
			}

			pw.close();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// returns empty string when things go wrong
	public static String[] read(String fileName, String relativePath) {
		String filePath = projectPath + relativePath + "/" + fileName;
		return read(filePath);
	}

//  returns empty string when things go wrong
	public static String[] read(String absolutePath) {
		String filePath = absolutePath;
		StringBuilder contents = new StringBuilder();
		
		//if(absolutePath == null) return "".split("");

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String temp;

			while ((temp = br.readLine()) != null) {
				contents.append(temp.toLowerCase() + " ");
			}

			br.close();

		}

		catch (IOException e) {
			e.printStackTrace();
		}

		return contents.toString().split(" ");
	}

	public static String readString(String absolutePath) {
		String filePath = absolutePath;
		StringBuilder contents = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String temp;

			while ((temp = br.readLine()) != null) {
				contents.append(temp + "\n");
			}

			br.close();

		}

		catch (IOException e) {
			// e.printStackTrace();
		}

		return contents.toString();
	}

	public static void main(String[] args) {
		System.out.println(getFileName("D:\\umair\\name.txt"));
	}

}
