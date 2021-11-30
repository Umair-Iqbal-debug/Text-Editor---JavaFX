package controller;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.TextEditor;
import utils.FileIO;

public class TextEditorController implements Initializable {
	
	@FXML
	private MenuItem logOutMenuItem;
	
	@FXML
	private HBox wordCountContainer;
	
	@FXML
	private HBox sentenceCountContainer;
	
	@FXML
	private HBox fleschScoreContainer;
	
	@FXML
	private Slider fontSlider;
	
	@FXML
	private VBox centerVBox;
	
	@FXML
	private MenuItem formatTextMenuItem;
	
	@FXML
	private HBox formatMenu;
	
	@FXML 
	private ComboBox<String> cboFontFamily;
	
	@FXML
	private ColorPicker colorPicker;

	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private Label fleschScoreLabel;

	@FXML
	private MenuItem fleschScoreMenuItem;

	@FXML
	private MenuItem loadMenuItem;

	@FXML
	private MenuItem markovTextBtn;

	@FXML
	private MenuBar menuBar;

	@FXML
	private MenuItem newMenuItem;

	@FXML
	private VBox rightVBox;

	@FXML
	private MenuItem saveAsMenuItem;

	@FXML
	private MenuItem saveMenuItem;

	@FXML
	private Label sentenceCountLabel;

	@FXML
	private MenuItem sentenceCountMenuItem;

	@FXML
	private CustomMenuItem spellCheckMenuItem;

	@FXML
	private TextArea textEditorTextArea;

	@FXML
	private MenuItem undoMenuItem;

	@FXML
	private MenuItem wordCounMenuItem;

	@FXML
	private Label wordCountLabel;

	private String fullPath = null;

	private TextArea misspelledWordsTextArea;

	private Stack<String> undoStack;

	private boolean spellCheck = false;

	private final TextEditor textEditor = new TextEditor();
	
	private final List<String> fontNames = Font.getFamilies();
	
	private boolean menuCollapsed;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		initfontCbo();
		
		Main.window.setTitle(Titles.TEXT_EDITOR);
		
		logOutMenuItem.setOnAction(e ->{
			// SAVE CHANGES CAN BE ITS OWN METHOD
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setHeaderText("Would you like to save the changes made to " + fullPath);
			// check if there is something unsaved
			boolean fullpathNull = fullPath == null;
			
			//read saved text file and compare content with current text area tex
			if(!fullpathNull && !hasSameWords(fullPath,textEditor.getWords())) {
				
				Optional<ButtonType> save = confirmation.showAndWait();
				
				if(save.get().equals(ButtonType.OK)) {
					FileIO.saveFile(textEditor.getRawContent(), new File(fullPath));
					System.out.println("Changes were saved");
				}
				
			}
			
			
			// close current file 
			closeMenuItem.fire();
			
			//if there something unchanged ask user if he wants to save it first
			
			
			//logout
			Main.bag.logout();
			
			// move back to login scene
			Main.window.setScene(Main.signInScene);
			
		});
		
		formatTextMenuItem.setOnAction(e ->{
			if(!menuCollapsed) {
				centerVBox.getChildren().remove(0);
				formatTextMenuItem.setText("Show Format Text");
				
			}
			else {
				centerVBox.getChildren().add(0,formatMenu);
				formatTextMenuItem.setText("Collapse");
			}
			
			menuCollapsed = !menuCollapsed;
		});
		
		colorPicker.setValue(Color.BLACK);
		colorPicker.valueProperty().addListener((obs,oldVal,newVal) ->{
			textEditorTextArea.setStyle("-fx-text-fill:" + toRgbString(newVal) + ";");
		});
		
		textEditorTextArea.setWrapText(true);
		misspelledWordsTextArea = new TextArea();

		undoStack = new Stack<>();

		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(new File("data"));
		loadMenuItem.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(null);

			if (file != null) {
				fullPath = file.getAbsolutePath();
				String contents = FileIO.readString(fullPath);
				textEditorTextArea.setText(contents);
			}
		});

		textEditorTextArea.textProperty().addListener((obs, oldVal, newVal) -> {
			textEditor.update(newVal);
			wordCountLabel.setText(textEditor.getTotalWords() + "");
			sentenceCountLabel.setText(textEditor.getTotalSentences() + "");

			if (oldVal.length() >= 0)
				undoStack.push(oldVal);
		});

		markovTextBtn.setOnAction(e -> {

			Pane root;
			try {
				root = FXMLLoader.load(getClass().getResource("../view/MarkovTextGenerator.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("../styles/application.css").toExternalForm());
				Main.window.setScene(scene);
				Main.window.show();
				
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});

		saveAsMenuItem.setOnAction(e -> {
			fullPath = FileIO.displayTextFileSaver(textEditor.getRawContent(), fileChooser);
		});

		saveMenuItem.setOnAction(e -> {
			if (fullPath == null)
				saveAsMenuItem.fire();
			else
				FileIO.saveFile(textEditor.getRawContent(), new File(fullPath));
		});

		// addIgnoredWordBtn.setOnAction(e -> {

//			dict.ignoreWord(ignoreWordTextField.getText().toLowerCase().strip());
//			ignoreWordTextField.clear();
//			String[] words = textEditorTextArea.getText().split(" ");
//			misspelledWordsTextArea.setText(dict.getWrongWordsString(words));

		// System.out.println(Arrays.toString(textEditor.getWords()));

		// });
		
		fontSlider.setValue(16);

		fontSlider.valueProperty().addListener(e -> {
			textEditorTextArea.setFont(Font.font(cboFontFamily.getValue(),fontSlider.getValue()));
		});
		
		
		closeMenuItem.setOnAction( e ->{
			
			// before closing make sure file is saved still need to add code to do that
			
			textEditorTextArea.clear();
			textEditor.update("");
			fullPath = null;
		});
		
		newMenuItem.setOnAction(e ->{
			// make sure current file is saved
			closeMenuItem.fire(); // closes previous file
			saveAsMenuItem.fire();
			
			String contents = FileIO.readString(fullPath);
			textEditorTextArea.setText(contents);

			
		});

		undoMenuItem.setOnAction(e -> {

			if (undoStack.isEmpty()) {
				textEditorTextArea.setText("");
				textEditor.update("");
				return;
			}

			String words = undoStack.pop();

			textEditorTextArea.setText(words);
			textEditor.update(words);

			if (!undoStack.isEmpty())
				undoStack.pop();

		});

		spellCheckMenuItem.setOnAction(e -> {
			if (!spellCheck) {
				initMisspelledWordsTextArea();
				misspelledWordsTextArea.setText(textEditor.getWrongWordsString());
			} else {
				rightVBox.getChildren().remove(0);
				spellCheck = false;
				rightVBox.setPrefSize(0, 0);
			}
			initTextEditor();
		});
		
		fleschScoreMenuItem.setOnAction(e ->{
			fleschScoreContainer.setVisible(!fleschScoreContainer.isVisible());
		});
		
		wordCounMenuItem.setOnAction(e ->{
			wordCountContainer.setVisible(!wordCountContainer.isVisible());
		});

		sentenceCountMenuItem.setOnAction(e ->{
			sentenceCountContainer.setVisible(!sentenceCountContainer.isVisible());
		});

	}

	private boolean hasSameWords(String fullPath2, String[] words) {
		
		String[] savedContents = FileIO.read(fullPath2);
		
		if(savedContents.length != words.length) return false;
		
		for(int i = 0; i < savedContents.length; i++) {
			if(!savedContents[i].equalsIgnoreCase(words[i])) return false;
		}
		
		return true;
	}

	private void initfontCbo() {
		
		cboFontFamily.getItems().addAll(fontNames);
		cboFontFamily.setValue(textEditorTextArea.getFont().getName());
		cboFontFamily.valueProperty().addListener((obs,oldVal,newVal) ->{
			
			textEditorTextArea.setFont(Font.font(newVal,fontSlider.getValue()));
		});
		
	}
	

	public void initMisspelledWordsTextArea() {
		misspelledWordsTextArea = new TextArea();
		VBox.setMargin(misspelledWordsTextArea, new Insets(5, 15, 0, 10));
		misspelledWordsTextArea.setPrefSize(94, 461);
		misspelledWordsTextArea.setStyle("-fx-text-fill: red;");
		rightVBox.setPrefSize(134, 482);
		rightVBox.setPadding(new Insets(5, 10, 5, 5));
		rightVBox.getChildren().add(misspelledWordsTextArea);
		rightVBox.setStyle("-fx-background-color:aliceblue;");
		spellCheck = true;
		misspelledWordsTextArea.fontProperty().bind(textEditorTextArea.fontProperty());
	}

	public void initTextEditor() {

		textEditorTextArea.setWrapText(true);
		textEditorTextArea.textProperty().addListener((obs, oldVal, newVal) -> {
			
			misspelledWordsTextArea.setText(textEditor.getWrongWordsString());

			// System.out.println(Arrays.toString(oldWords));

		});
	}
	
	private String toRgbString(Color c) {
        return "rgb("
                          + to255Int(c.getRed())
                    + "," + to255Int(c.getGreen())
                    + "," + to255Int(c.getBlue())
             + ")";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }

}
