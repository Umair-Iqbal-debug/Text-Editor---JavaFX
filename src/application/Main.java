package application;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Optional;

import controller.SignInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.UserBag;
import utils.BackUpRestoreTools;

public class Main extends Application {

	public static UserBag bag = BackUpRestoreTools.restoreUserBag();
	public static AnchorPane signUpPane;
	public static AnchorPane signInPane;
	public static SplitPane splitPane;
	public static Image errorImage;
	public static Image checkMarkImage;
	public static Stage window;
	public static Parent textEditor;
	public static Scene textEditorScene;
	public static Scene signInScene;
	public static final String VIEW_BASE_PATH = "../view/";
	public static final String SIGN_IN_VIEW_PATH = VIEW_BASE_PATH + "SignIn.fxml";
	public static final String SIGN_UP_VIEW_PATH = VIEW_BASE_PATH + "SignUp.fxml";
	public static final String TEXT_EDITOR_VIEW_PATH = VIEW_BASE_PATH + "TextEditor.fxml";
	public static final String MARKOV_VIEW_PATH = VIEW_BASE_PATH + "MarkovTextGenerator.fxml";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		window = stage;

		initOnClosingWindow();

		initIcons();

		initSignUpPane();

		initTextEditor();

		startUp();

	}

	public void initSignUpPane() throws IOException {
		signUpPane = (AnchorPane) loadResource(SIGN_UP_VIEW_PATH);
	}

	public void initTextEditor() throws IOException {
		textEditor = (Parent) loadResource(TEXT_EDITOR_VIEW_PATH);

		textEditorScene = new Scene(textEditor);
	}

	public void initIcons() throws Exception {

		FileInputStream input = new FileInputStream("resources\\images\\icons\\xMarkIcon.jpg");

		errorImage = new Image(input);

		input = new FileInputStream("resources\\images\\icons\\checkMarkIcon.jpg");
		checkMarkImage = new Image(input);
	}

	public Parent loadResource(String resourcePath) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(resourcePath));
		Parent view = (Parent) loader.load();
		return view;
	}

	public void startUp() throws IOException {

		FXMLLoader loader1 = new FXMLLoader();
		loader1.setLocation(getClass().getResource(SIGN_IN_VIEW_PATH));
		Parent signIn = (Parent) loader1.load();

		SignInController controller = loader1.getController();
		signInPane = controller.getRoot();
		splitPane = controller.getSplitPane();

		signInScene = new Scene(signIn);

		signInScene.getStylesheets().add(getClass().getResource("../styles/application.css").toExternalForm());

		window.setTitle("Sign In");
		window.setScene(signInScene);
		window.show();
	}

	public void initOnClosingWindow() {

		window.setOnCloseRequest(e -> {
			e.consume(); // prevent it from closing

			// check if user is logged in
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Are you sure you want to exit ?");
			alert.setHeaderText("You will be logged out and all unsaved data will be lost!");
			Optional<ButtonType> choice = alert.showAndWait();

			if (choice.get().equals(ButtonType.OK)) {
				if (bag.isLoggedIn()) {
					bag.logout();
				}
				window.close();
			}
		});
	}
	
	

}
