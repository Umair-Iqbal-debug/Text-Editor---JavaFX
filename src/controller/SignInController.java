package controller;

import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SignInController implements Initializable {

	@FXML
	private Button signUpBtn;

	@FXML
	private SplitPane splitPane;

	@FXML
	private Button loginBtn;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField passwordFieldContainer;

	@FXML
	private AnchorPane rightHalfAnchorPane;

	@FXML
	private Button showPasswordBtn;

	@FXML
	private TextField showPasswordField;

	@FXML
	private TextField usernameTextField;

	@FXML
	private Label errorLabel;
	
	// NEEDS REFACTORING
	
	public AnchorPane getRoot() {
		return rightHalfAnchorPane;
	}

	public SplitPane getSplitPane() {
		return splitPane;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		errorLabel.setText("");
		
		Main.window.setTitle(Titles.SIGN_IN);

		/*
		 * showPasswordField overflows to the hide button add another textfield just for
		 * border make showPasswordField transparent
		 */

		showPasswordField.textProperty().bindBidirectional(passwordField.textProperty());
		showPasswordField.setVisible(false);
		showPasswordBtn.setOnAction(e -> {

			if (showPasswordField.isVisible()) {
				showPasswordField.setVisible(false);
				passwordField.setVisible(true);
				showPasswordBtn.setText("Show");
				passwordField.requestFocus();
				passwordField.end();
			}

			else {
				passwordField.setVisible(false);
				showPasswordField.setVisible(true);
				showPasswordBtn.setText("Hide");
				showPasswordField.requestFocus();
				showPasswordField.end();
			}

		});

		passwordField.focusedProperty().addListener((obs, newVal, oldVal) -> {
			if (oldVal)
				passwordFieldContainer.setStyle("-fx-border-color: blue blue blue blue;");
			else
				passwordFieldContainer.setStyle("-fx-border-color: rgba(104, 92, 138, 0.2);");
		});

		showPasswordField.focusedProperty().addListener((obs, newVal, oldVal) -> {
			if (oldVal)
				passwordFieldContainer.setStyle("-fx-border-color: blue blue blue blue;");
			else
				passwordFieldContainer.setStyle("-fx-border-color: rgba(104, 92, 138, 0.2);");
		});

		signUpBtn.setOnAction(e -> {
			Main.window.setTitle(Titles.SIGN_UP);
			splitPane.getItems().set(1, Main.signUpPane);

		});

		loginBtn.setOnAction(e -> {

			if (usernameTextField.getText().isBlank()) {

				usernameTextField.setStyle("-fx-border-color:red;");
				errorLabel.setText("Username cannot be blank!");
			}

			else if (passwordField.getText().isBlank()) {

				passwordFieldContainer.setStyle("-fx-border-color:red;");
				errorLabel.setText("Password cannot be blank!");
			}

			else if (!Main.bag.isUsernameTaken(usernameTextField.getText())) {
				
				usernameTextField.setStyle("-fx-border-color:red;");
				errorLabel.setText("Sorry we couldn't find any account with that username.");
			}

			else if (Main.bag.login(usernameTextField.getText(), passwordField.getText())) {

				// move text editor here
				Main.window.setTitle(Titles.TEXT_EDITOR);
				Main.window.setScene(Main.textEditorScene);
				
				passwordField.clear();
				passwordField.setPromptText("PASSWORD");
				usernameTextField.clear();
			}

			else {
				passwordFieldContainer.setStyle("-fx-border-color:red;");
				errorLabel.setText("Incorrect password , please try again. ");
			}
		});

	}

}
