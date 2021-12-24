package controller;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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

	private static final double DEFAULT_FONT_SIZE = 16;

	@FXML
	private VBox topVBox;

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
	private MenuItem liveSpellCheckMenuItem;

	@FXML
	private Label wordCountLabel;

	private String fullPath = null;

	private TextArea misspelledWordsTextArea;

	private boolean spellCheck = false;

	private final TextEditor textEditor = new TextEditor();

	private final List<String> fontNames = Font.getFamilies();

	private boolean menuCollapsed;

	private boolean liveSpellCheckEnabled = true;

	final FileChooser fileChooser = new FileChooser();

	private Insets paddingWithFormatMenuUncollapsed = new Insets(0, 10, 5, 5);

	private Insets paddingWithoutFormatMenu = new Insets(2.5, 10, 5, 5);

	ChangeListener<String> updateStats = this::updateStats;
	EventHandler<KeyEvent> updateStatsOnSpace = this::updateStatsOnSpace;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initfontCbo();
		misspelledWordsTextArea = new TextArea();
		initShortcutKeys();

		textEditorTextArea.setPrefHeight(centerVBox.getHeight());

		Main.window.setTitle(Titles.TEXT_EDITOR);

		exitMenuItem.setOnAction(this::exitMenuHandler);

		logOutMenuItem.setOnAction(this::logoutMenuItemHandler);

		formatTextMenuItem.setOnAction(this::formatTextMenuHandler);

		initColorPicker();

		textEditorTextArea.setWrapText(true);
		misspelledWordsTextArea = new TextArea();

		configureFileChooser();

		loadMenuItem.setOnAction(this::loadMenuItemHandler);

		textEditorTextArea.textProperty().addListener(updateStats);

		// textEditorTextArea.setOnKeyPressed(this::updateStatsOnSpace);

		markovTextBtn.setOnAction(this::markovTextBtnHandler);

		saveAsMenuItem.setOnAction(this::saveAsMenuItemHandler);

		saveMenuItem.setOnAction(this::saveMenuItemHandler);

		fontSlider.setValue(DEFAULT_FONT_SIZE);

		fontSlider.valueProperty().addListener(this::fontValuePropertyHandler);

		closeMenuItem.setOnAction(this::closeMenuItemHandler);

		newMenuItem.setOnAction(this::newMenuItemHandler);

		undoMenuItem.setOnAction(e -> textEditorTextArea.undo());

		spellCheckMenuItem.setOnAction(this::spellCheckHandler);

		liveSpellCheckMenuItem.setOnAction(this::liveSpellCheck);

		fleschScoreMenuItem.setOnAction(this::fleschScoreHandler);

		wordCounMenuItem.setOnAction(this::wordCountHandler);

		sentenceCountMenuItem.setOnAction(this::sentenceCountHandler);

		centerVBox.heightProperty().addListener(this::resizeTextEditorTextArea);

		liveSpellCheckMenuItem.setText("Disable live Spell-Check");
		
	}

	public void liveSpellCheck(ActionEvent e) {
		// if live spell check is already enabled and we are click on it disable it and
		// enable spell check on space
		if (liveSpellCheckEnabled)
			disableLiveSpellCheck();

		else
			enableLiveSpellCheck();

		liveSpellCheckEnabled = !liveSpellCheckEnabled;
	}

	private void disableLiveSpellCheck() {
		// System.out.println("disabling live spell-check");
		// show some message to let them know what is enabled
		textEditorTextArea.textProperty().removeListener(updateStats);
		textEditorTextArea.setOnKeyPressed(updateStatsOnSpace);
		liveSpellCheckMenuItem.setText("Enable live Spell-Check");

	}

	private void enableLiveSpellCheck() {
		textEditorTextArea.removeEventHandler(KeyEvent.KEY_PRESSED, updateStatsOnSpace);
		textEditorTextArea.textProperty().addListener(updateStats);
		updateStats();
		misspelledWordsTextArea.setText(textEditor.getWrongWordsString());
		liveSpellCheckMenuItem.setText("Disable live Spell-Check");

	}

	public void updateStatsOnSpace(KeyEvent e) {
		if (e.getCode() == KeyCode.SPACE)
			updateStats();
	}

	public void exitMenuHandler(ActionEvent e) {
		logOutMenuItem.fire();
		Main.window.close();
	}

	public void logoutMenuItemHandler(ActionEvent e) {
		// close current file
		closeMenuItem.fire();

		// logout
		Main.bag.logout();

		// move back to login scene
		moveToSignInPage();
	}

	public void formatTextMenuHandler(ActionEvent e) {

		if (!menuCollapsed)
			collapseFormatTextMenu();
		else
			unCollapseFormatTextMenu();

		menuCollapsed = !menuCollapsed;
	}

	public void unCollapseFormatTextMenu() {
		topVBox.getChildren().add(1, formatMenu);
		formatTextMenuItem.setText("Collapse Format Menu");
		rightVBox.setPadding(paddingWithFormatMenuUncollapsed);
		misspelledWordsTextArea.setPrefWidth(94);
		textEditorTextArea.setPrefHeight(centerVBox.getHeight());
		misspelledWordsTextArea.setPrefHeight(rightVBox.getHeight());
	}

	public void collapseFormatTextMenu() {

		topVBox.getChildren().remove(1);
		formatTextMenuItem.setText("Show Format Text Menu");
		rightVBox.setPadding(paddingWithoutFormatMenu);
		topVBox.setPrefHeight(topVBox.getHeight() - 40);
		textEditorTextArea.setPrefHeight(centerVBox.getHeight());
		misspelledWordsTextArea.setPrefHeight(rightVBox.getHeight());
		VBox.setMargin(textEditorTextArea, new Insets(5, 5, 5, 5));
	}

	public void initColorPicker() {
		colorPicker.setValue(Color.BLACK);
		colorPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
			textEditorTextArea.setStyle("-fx-text-fill:" + toRgbString(newVal) + ";");
		});
	}

	public void loadMenuItemHandler(ActionEvent e) {

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			fullPath = file.getAbsolutePath();
			String contents = FileIO.readString(fullPath);
			textEditorTextArea.setText(contents);
			fileNameLabel.setText(FileIO.getFileName(fullPath));
		}
	}

	public void markovTextBtnHandler(ActionEvent e) {

		Pane root;
		try {
			root = FXMLLoader.load(getClass().getResource(Main.MARKOV_VIEW_PATH));
			Scene scene = new Scene(root);
			applyBaseStyle(scene);
			Main.window.setScene(scene);
			Main.window.show();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void configureFileChooser() {
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(new File("data"));
	}

	public void saveAsMenuItemHandler(ActionEvent e) {

		fullPath = FileIO.displayTextFileSaver(textEditor.getRawContent(), fileChooser);
		if (fullPath != null)
			fileNameLabel.setText(FileIO.getFileName(fullPath));
	}

	public void saveMenuItemHandler(ActionEvent e) {
		if (fullPath == null || fullPath.isBlank()) {
			saveAsMenuItem.fire();
			// System.out.println("Save as menu item fired");
		}

		else
			FileIO.saveFile(textEditor.getRawContent(), new File(fullPath));
		fileNameLabel.setText(FileIO.getFileName(fullPath));
	}

	public void fontValuePropertyHandler(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
		textEditorTextArea.setFont(Font.font(cboFontFamily.getValue(), fontSlider.getValue()));
	}

	public void closeMenuItemHandler(ActionEvent e) {
		if (fullPath != null)
			saveChanges();
		textEditorTextArea.clear();
		textEditor.update("");
		fullPath = null;
		fileNameLabel.setText("No File Selected");
	}

	public void newMenuItemHandler(ActionEvent e) {
		// make sure current file is saved
		if (isFileOpen())
			closeMenuItem.fire(); // closes previous file

	}

	public boolean isFileOpen() {
		return fullPath != null;
	}

	public void sentenceCountHandler(ActionEvent e) {
		sentenceCountContainer.setVisible(!sentenceCountContainer.isVisible());
	}

	public void wordCountHandler(ActionEvent e) {
		wordCountContainer.setVisible(!wordCountContainer.isVisible());
	}

	public void fleschScoreHandler(ActionEvent e) {
		fleschScoreContainer.setVisible(!fleschScoreContainer.isVisible());
	}

	public void resizeTextEditorTextArea(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
		textEditorTextArea.setPrefHeight((double) newVal);
	}
// WORKING HAS_SAME_WORDS
//	private boolean hasSameWords(String fullPath2, String[] words) {
//
//		String[] savedContents = FileIO.read(fullPath2);
//
//		if (savedContents.length != words.length)
//			return false;
//
//		for (int i = 0; i < savedContents.length; i++) {
//			if (!savedContents[i].equalsIgnoreCase(words[i]))
//				return false;
//		}
//
//		return true;
//	}
	
	private boolean hasSameWords(String fullPath2, String words) {

		String savedContents = FileIO.readString(fullPath2);

		if (savedContents.length() != words.length())
			return false;

		return savedContents.equals(words);

	}

	private void initfontCbo() {

		cboFontFamily.getItems().addAll(fontNames);
		cboFontFamily.setValue(textEditorTextArea.getFont().getName());
		cboFontFamily.valueProperty().addListener((obs, oldVal, newVal) -> {

			textEditorTextArea.setFont(Font.font(newVal, fontSlider.getValue()));
		});

	}

	public void initMisspelledWordsTextArea() {

		VBox.setMargin(misspelledWordsTextArea, new Insets(5, 5, 0, 10));
		misspelledWordsTextArea.setPrefWidth(94);
		misspelledWordsTextArea.setPrefHeight(rightVBox.getHeight());
		misspelledWordsTextArea.setStyle("-fx-text-fill: red;");
		rightVBox.setPrefSize(134, 482);
		rightVBox.setPadding(paddingWithFormatMenuUncollapsed);
		rightVBox.getChildren().add(misspelledWordsTextArea);
		rightVBox.setStyle("-fx-background-color:aliceblue;");
		spellCheck = true;
		misspelledWordsTextArea.setEditable(false);
	}

	public void resizeMisspelledTextArea(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
		misspelledWordsTextArea.setPrefHeight((double) newVal);
	}

	public void enableSpellCheck() {

		initMisspelledWordsTextArea();
		misspelledWordsTextArea.setText(textEditor.getWrongWordsString());
		textEditorTextArea.setWrapText(true);
		rightVBox.heightProperty().addListener(this::resizeMisspelledTextArea);
	}

	public void disableSpellCheck() {
		rightVBox.getChildren().remove(0);
		spellCheck = false;
		rightVBox.setPrefSize(0, 0);
		rightVBox.heightProperty().removeListener(this::resizeMisspelledTextArea);
	}

	public boolean changesWereMade() {
		return !hasSameWords(fullPath, textEditorTextArea.getText());

	}

	public void updateStats(ObservableValue<? extends String> obs, String oldVal, String newVal) {

		textEditor.update(newVal);
		updateStatusBarLabels();
		updateCurrFilePath();
		misspelledWordsTextArea.setText(textEditor.getWrongWordsString());

	}

	public void updateStats() {

		textEditor.update(textEditorTextArea.getText());
		updateStatusBarLabels();
		updateCurrFilePath();
		misspelledWordsTextArea.setText(textEditor.getWrongWordsString());

	}

	public void updateCurrFilePath() {

		if (fullPath != null) {
			if (changesWereMade())
				fileNameLabel.setText("*" + FileIO.getFileName(fullPath));
			else
				fileNameLabel.setText(FileIO.getFileName(fullPath));
		}

		else {
			fileNameLabel.setText(FileIO.getFileName(fullPath));
		}
	}

	public void updateStatusBarLabels() {

		wordCountLabel.setText(textEditor.getTotalWords() + "");
		sentenceCountLabel.setText(textEditor.getTotalSentences() + "");
		fleschScoreLabel.setText(formatDoubleVal(textEditor.getFleschScore()));

	}

	public void spellCheckHandler(ActionEvent e) {

		if (!spellCheck)
			enableSpellCheck();

		else
			disableSpellCheck();
	}

	public void saveChanges() {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);

		// check if there is something unsaved
		boolean fullpathNull = fullPath == null;

		// read saved text file and compare content with current text area tex
		if (!fullpathNull && !hasSameWords(fullPath, textEditorTextArea.getText())) {

			confirmation.setHeaderText("Would you like to save the changes made to " + FileIO.getFileName(fullPath));

			Optional<ButtonType> save = confirmation.showAndWait();

			if (save.get().equals(ButtonType.OK)) {
				FileIO.saveFile(textEditor.getRawContent(), new File(fullPath));
				// System.out.println("Changes were saved");
			}

		}

		else if (fullpathNull && textEditor.hasContent()) {

			confirmation
					.setHeaderText("You are closing the file without saving the content, would you like to save it ?");
			Optional<ButtonType> save = confirmation.showAndWait();

			if (save.get().equals(ButtonType.OK)) {
				saveAsMenuItem.fire();
			}

		}

	}

	public void moveToSignInPage() {

		FXMLLoader loader1 = new FXMLLoader();
		loader1.setLocation(getClass().getResource(Main.SIGN_IN_VIEW_PATH));
		Parent signIn;
		try {
			signIn = (Parent) loader1.load();
			SignInController controller = loader1.getController();
			Main.signInPane = controller.getRoot();
			Main.splitPane = controller.getSplitPane();

			Main.signInScene = new Scene(signIn);

			Main.signInScene.getStylesheets().add(getClass().getResource("../styles/application.css").toExternalForm());

			Main.window.setTitle("Sign In");
			Main.window.setScene(Main.signInScene);
			Main.window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void initShortcutKeys() {

		saveMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		loadMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		closeMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
		newMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		formatTextMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
		undoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		liveSpellCheckMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));

	}

	public void applyBaseStyle(Scene scene) {
		scene.getStylesheets().add(getClass().getResource("../styles/application.css").toExternalForm());
	}

	// METHODS THAT SHOULD GO TO A DIFFERENT CLASS

	private String toRgbString(Color c) {
		return "rgb(" + to255Int(c.getRed()) + "," + to255Int(c.getGreen()) + "," + to255Int(c.getBlue()) + ")";
	}

	private int to255Int(double d) {
		return (int) (d * 255);

	}

	static String formatDoubleVal(double val) {
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return formatter.format(val);
	}

}
