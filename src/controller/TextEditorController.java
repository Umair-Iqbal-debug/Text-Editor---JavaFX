package controller;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
	private HBox currentFileHBox;
	
	@FXML
	private Label fileNameLabel;
	
	@FXML
	private MenuItem exitMenuItem;
	
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
		
		exitMenuItem.setOnAction(e ->{
			logOutMenuItem.fire();
			Main.window.close();
		});
		
		
		
		logOutMenuItem.setOnAction(e ->{
			// SAVE CHANGES CAN BE ITS OWN METHOD
			
			saveChanges();
			
			// close current file 
			closeMenuItem.fire();
			
			//if there something unchanged ask user if he wants to save it first
			
			
			//logout
			Main.bag.logout();
			
			// move back to login scene
			
			try {
				FXMLLoader loader1 = new FXMLLoader();
				loader1.setLocation(getClass().getResource(Main.SIGN_IN_VIEW_PATH));
				Parent signIn = (Parent) loader1.load();

				SignInController controller = loader1.getController();
				Main.signInPane = controller.getRoot();
				Main.splitPane = controller.getSplitPane();

				Main.signInScene = new Scene(signIn);

				Main.signInScene.getStylesheets().add(getClass().getResource("../styles/application.css").toExternalForm());

				Main.window.setTitle("Sign In");
				Main.window.setScene(Main.signInScene);
				Main.window.show();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
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
				fileNameLabel.setText(FileIO.getFileName(fullPath));	
			}
		});

		textEditorTextArea.textProperty().addListener((obs, oldVal, newVal) -> {
			textEditor.update(newVal);
			wordCountLabel.setText(textEditor.getTotalWords() + "");
			sentenceCountLabel.setText(textEditor.getTotalSentences() + "");
			fleschScoreLabel.setText(formatDoubleVal(textEditor.getFleschScore()));
			if(fullPath != null) {
				//System.out.println("full path is not null");
				if(changesWereMade()) fileNameLabel.setText("*" + FileIO.getFileName(fullPath));
				else fileNameLabel.setText(FileIO.getFileName(fullPath));
			}
			
			else {
				fileNameLabel.setText(FileIO.getFileName(fullPath));
				//System.out.println("full path is null");
			}
			
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
			fileNameLabel.setText(FileIO.getFileName(fullPath));
		});

		saveMenuItem.setOnAction(e -> {
			if (fullPath == null)
				saveAsMenuItem.fire();
			else
				FileIO.saveFile(textEditor.getRawContent(), new File(fullPath));
			fileNameLabel.setText(FileIO.getFileName(fullPath));
		});


		fontSlider.setValue(16);

		fontSlider.valueProperty().addListener(e -> {
			textEditorTextArea.setFont(Font.font(cboFontFamily.getValue(),fontSlider.getValue()));
		});
		
		
		closeMenuItem.setOnAction( e ->{
			
			// before closing make sure file is saved still need to add code to do that
			saveChanges();
			textEditorTextArea.clear();
			textEditor.update("");
			fullPath = null;
			fileNameLabel.setText("None");
		});
		
		newMenuItem.setOnAction(e ->{
			// make sure current file is saved
			closeMenuItem.fire(); // closes previous file
			saveAsMenuItem.fire();
			
			String contents = FileIO.readString(fullPath);
			textEditorTextArea.setText(contents);
			fileNameLabel.setText(FileIO.getFileName(fullPath));

			
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
    
    static String formatDoubleVal(double val){
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(val);
    }
    
   public boolean changesWereMade() {
		    	
		    	boolean res = !hasSameWords(fullPath,textEditor.getWords());
		    	
		    	if(res) System.out.println("changes were made");
		    	
		    	return res;
    }
    
    public void saveChanges() {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		
		// check if there is something unsaved
		boolean fullpathNull = fullPath == null;
		
		//read saved text file and compare content with current text area tex
		if(!fullpathNull && !hasSameWords(fullPath,textEditor.getWords())) {

			confirmation.setHeaderText("Would you like to save the changes made to " + fullPath);
			
			Optional<ButtonType> save = confirmation.showAndWait();
			
			if(save.get().equals(ButtonType.OK)) {
				FileIO.saveFile(textEditor.getRawContent(), new File(fullPath));
				System.out.println("Changes were saved");
			}
			
		}
		
		else if(fullpathNull && textEditor.hasContent()) {
			
			confirmation.setHeaderText("You are closing the file without saving the content, would you like to save it ?");
			Optional<ButtonType> save = confirmation.showAndWait();
			
			if(save.get().equals(ButtonType.OK)) {
				saveAsMenuItem.fire();
			}
			
		}
		
	}
    
    

}
