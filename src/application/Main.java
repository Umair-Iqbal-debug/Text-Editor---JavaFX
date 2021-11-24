package application;

import java.io.FileInputStream;

import controller.SignInController;
import controller.SignUpController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.GlobalData;
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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		window = stage;
		
		FileInputStream input = new FileInputStream("resources\\images\\icons\\xMarkIcon.jpg");
		
		errorImage= new Image(input);
		
		
		 input = new FileInputStream("resources\\images\\icons\\checkMarkIcon.jpg");
		 checkMarkImage = new Image(input);
		 
		 

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../view/SignUp.fxml"));
		Parent signUp = (Parent) loader.load();

		signUpPane = (AnchorPane) signUp;
		
		
		FXMLLoader loader2 = new FXMLLoader();
		loader2.setLocation(getClass().getResource("../view/TextEditor.fxml"));
		 textEditor = (Parent) loader2.load();
		 
		 textEditorScene = new Scene(textEditor);

		
		
		
		FXMLLoader loader1 = new FXMLLoader();
		loader1.setLocation(getClass().getResource("../view/SignIn.fxml"));
		Parent signIn = (Parent) loader1.load();
		
		SignInController controller = loader1.getController();
		signInPane = controller.getRoot();
		splitPane = controller.getSplitPane();


		//Parent root = FXMLLoader.load(getClass().getResource("../view/SignIn.fxml"));

		 signInScene = new Scene(signIn);

		 signInScene.getStylesheets().add(getClass().getResource("../styles/application.css").toExternalForm());

		stage.setTitle("Sign In");
		stage.setScene(signInScene);
		stage.show();
	}
}
