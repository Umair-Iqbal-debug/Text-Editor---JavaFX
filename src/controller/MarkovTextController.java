package controller;

import java.io.File;


import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.MarkovTextGeneratorBST;

public class MarkovTextController implements Initializable {

	private String absoluteFilePath = null;

	@FXML
	private MenuItem chooseFileField;

	@FXML
	private Button backBtn;

	@FXML
	private TextField numberField;

	@FXML
	private TextArea outputField;

	@FXML
	private Button learnBtn;

	@FXML
	private Button createBtn;

	@FXML
	private TextField wordField;

	private MarkovTextGeneratorBST gen;

	@FXML
	private Label fileNameLabel;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Main.window.setTitle(Titles.MARKOV_TEXT);
		
		initLearnBtn();

		gen = new MarkovTextGeneratorBST();
		
		outputField.setWrapText(true);

		createBtn.setOnAction(e -> {

			// input validation & make sure there is a file selected

//			if(absoluteFilePath == null) {
//				errorLabel.setText("no file selected");
//			}
//			
//			else if(wordField.getText().isEmpty()) {
//				errorLabel.setText("total words cannot be empty");
//			}
//			
//			else if(wordField.getText().isEmpty()) {
//				errorLabel.setText("starter word cannot be empty");
//			}
//			

			// if totalWords.getText().isEmpty() show error message

			// if starterWords.getText().isEmpty() show error message

			int totalWords = Integer.parseInt(numberField.getText());
			String starterWord = wordField.getText();

			outputField.setText(gen.generateText(totalWords, starterWord));

		});

		numberField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.isEmpty() && !isNumeric(newVal))
				numberField.setText(oldVal);
			if (newVal.isEmpty())
				createBtn.setDisable(true);
			else
				createBtn.setDisable(false);
		});

		backBtn.setOnAction(e -> {
			Main.window.setTitle(Titles.TEXT_EDITOR);
			Main.window.setScene(Main.textEditorScene);
			// Main.window.show();
		});
	}

	public boolean isNumeric(String string) {

		for (char curr : string.toCharArray()) {
			if (!Character.isDigit(curr))
				return false;
		}

		return true;
	}

	public void initLearnBtn() {
		createBtn.setDisable(true);
		final FileChooser fileChooser = new FileChooser();

		learnBtn.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(null);

			if (file != null) {
				absoluteFilePath = file.getAbsolutePath();
				fileNameLabel.setText(file.getName());
				gen.train(utils.FileIO.read(absoluteFilePath));
			}
		});
	}
}