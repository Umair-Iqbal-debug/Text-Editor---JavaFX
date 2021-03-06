package controller;


import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Password;
import utils.BackUpRestoreTools;

public class SignUpController implements Initializable {
	
	private static final String GREEN_TEXT = "-fx-text-fill:green;";
	
	private static final String RED_TEXT = "-fx-text-fill:red;";
	@FXML
	private Label usernameIsTakenLabel;
	@FXML
    private ImageView passwordsMustMatchIcon;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordFieldContainer;

    @FXML
    private ImageView passwordDigitIcon;

    @FXML
    private Label passwordDigitLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordFieldContainer;

    @FXML
    private ImageView passwordLengthIcon;

    @FXML
    private Label passwordLengthLabel;

    @FXML
    private ImageView passwordLowerCaseIcon;

    @FXML
    private Label passwordLowerCaseLabel;

    @FXML
    private ImageView passwordUpperCaseIcon;

    @FXML
    private Label passwordUpperCaseLabel;

    @FXML
    private Label passwordsMustMatchLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField showConfirmPasswordField;

    @FXML
    private Button showPasswordBtn;

    @FXML
    private Button showPasswordBtn1;

    @FXML
    private TextField showPasswordField;

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField usernameTextField;
    

	public AnchorPane getRoot() {
		return root;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		signUpBtn.setDisable(true);

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

		showConfirmPasswordField.textProperty().bindBidirectional(confirmPasswordField.textProperty());
		showConfirmPasswordField.setVisible(false);

		showPasswordBtn1.setOnAction(e -> {

			if (showConfirmPasswordField.isVisible()) {
				showConfirmPasswordField.setVisible(false);
				confirmPasswordField.setVisible(true);
				showPasswordBtn1.setText("Show");
				confirmPasswordField.requestFocus();
				confirmPasswordField.end();
			}

			else {
				confirmPasswordField.setVisible(false);
				showConfirmPasswordField.setVisible(true);
				showPasswordBtn1.setText("Hide");
				showConfirmPasswordField.requestFocus();
				showConfirmPasswordField.end();
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

		confirmPasswordField.focusedProperty().addListener((obs, newVal, oldVal) -> {
			if (oldVal)
				confirmPasswordFieldContainer.setStyle("-fx-border-color: blue blue blue blue;");
			else
				confirmPasswordFieldContainer.setStyle("-fx-border-color: rgba(104, 92, 138, 0.2);");
		});

		showConfirmPasswordField.focusedProperty().addListener((obs, newVal, oldVal) -> {
			if (oldVal)
				confirmPasswordFieldContainer.setStyle("-fx-border-color: blue blue blue blue;");
			else
				confirmPasswordFieldContainer.setStyle("-fx-border-color: rgba(104, 92, 138, 0.2);");
		});

		passwordField.textProperty().addListener((obs, oldVal, newVal) -> {

			String confirmPass = confirmPasswordField.getText();

			
			if(Password.upperCaseCheck.test(newVal)) setTestPassedStyle(passwordUpperCaseIcon,passwordUpperCaseLabel);
			
			else setTestFailedStyle(passwordUpperCaseIcon,passwordUpperCaseLabel);
							
			
			if(Password.lowerCaseCheck.test(newVal))setTestPassedStyle(passwordLowerCaseIcon,passwordLowerCaseLabel);
			else setTestFailedStyle(passwordLowerCaseIcon,passwordLowerCaseLabel);
			
			if(Password.lengthCheck.test(newVal)) setTestPassedStyle(passwordLengthIcon,passwordLengthLabel);
			else  setTestFailedStyle(passwordLengthIcon,passwordLengthLabel);
			
			if(Password.digitCheck.test(newVal))  setTestPassedStyle(passwordDigitIcon,passwordDigitLabel);
			else setTestFailedStyle(passwordDigitIcon,passwordDigitLabel);
			

			if (!confirmPass.isEmpty()) {
				if (!confirmPass.equals(newVal)) {
					passwordsMustMatchIcon.setImage(Main.errorImage);
					setErrorStyleOnFields();
				}
				
				else {
					passwordsMustMatchIcon.setImage(Main.checkMarkImage);
					if(Password.validatePassword(newVal)) signUpBtn.setDisable(false);
					
					 resetStyle();
				}
			}

		});

		confirmPasswordField.textProperty().addListener((obs, oldVal,newVal ) -> {

			String pass = passwordField.getText();

			if (!pass.isEmpty()) {
				if (!pass.equals(newVal)) {
					setTestFailedStyle(passwordsMustMatchIcon,passwordsMustMatchLabel);
					setErrorStyleOnFields();
				}
				
				else {
					setTestPassedStyle(passwordsMustMatchIcon,passwordsMustMatchLabel);
					if(Password.validatePassword(newVal) && !usernameTextField.getText().isBlank()) signUpBtn.setDisable(false);
					resetStyle();
				}
			}

		});
		
		usernameTextField.textProperty().addListener((obs,oldVal,newVal) ->{
			String username = usernameTextField.getText();
			boolean usernameIsavailable = !Main.bag.isUsernameTaken(username);
			if(Password.validatePassword(passwordField.getText()) && usernameIsavailable) signUpBtn.setDisable(false);
			else if(Main.bag.isUsernameTaken(username)) {
				usernameIsTakenLabel.setVisible(true);
				signUpBtn.setDisable(true);
			}
			else usernameIsTakenLabel.setVisible(false);
		});

		signInBtn.setOnAction(e -> {
			Main.window.setTitle(Titles.SIGN_IN);
			Main.splitPane.getItems().set(1, Main.signInPane);
		});
		
		signUpBtn.setOnAction(e ->{
			
			if (usernameTextField.getText().isBlank()) {

				usernameTextField.setStyle("-fx-border-color:red;");
				//errorLabel.setText("Username cannot be blank!");
			}

			else if (passwordField.getText().isBlank()) {

				passwordFieldContainer.setStyle("-fx-border-color:red;");
				//errorLabel.setText("Password cannot be blank!");
			}

			else if (Main.bag.isUsernameTaken(usernameTextField.getText())) {
				
				usernameTextField.setStyle("-fx-border-color:red;");
				

			}

			else if (Main.bag.addUser(usernameTextField.getText(), passwordField.getText())) {
				BackUpRestoreTools.backupUserBag(Main.bag);
				// move text editor here
				Main.window.setTitle(Titles.TEXT_EDITOR);
				Main.window.setScene(Main.textEditorScene);

				//errorLabel.setText("Sign up was successful!");
				passwordField.clear();
				usernameTextField.clear();
				confirmPasswordField.clear();
			}

			else {
				passwordFieldContainer.setStyle("-fx-border-color:red;");
				
				//errorLabel.setText("Password must be atleast 6 characters with atleast one uppercase,lowercase and a digit!");
			}
		});
	}
	
	public void setTestFailedStyle(ImageView imageView,Label label) {
		imageView.setImage(Main.errorImage);
		label.setStyle(RED_TEXT);
	}
	
	public void setTestPassedStyle(ImageView imageView,Label label) {
		imageView.setImage(Main.checkMarkImage);
		label.setStyle(GREEN_TEXT);
	}

	public void setErrorStyleOnFields() {
		passwordFieldContainer.setStyle("-fx-border-color:red;");
		confirmPasswordFieldContainer.setStyle("-fx-border-color:red;");
	}
	
	public void resetStyle() {
		passwordFieldContainer.setStyle("-fx-border-color: rgba(104, 92, 138, 0.2);");
		confirmPasswordFieldContainer.setStyle("-fx-border-color: rgba(104, 92, 138, 0.2);");
	}

}
