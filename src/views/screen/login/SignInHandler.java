package views.screen.login;

import java.io.IOException;
import java.sql.SQLException;

import controller.LoginController;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

public class SignInHandler extends BaseScreenHandler{
	
	@FXML
	private Button signIn;
	
	@FXML
	private Button signUp;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password1;
	
	@FXML
	private PasswordField password2;
	
	private static LoginController controller;

	public SignInHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
		controller = new LoginController();
		signIn.setOnMousePressed(e->{
			prev.show();
		});
		signUp.setOnMouseClicked(e->{
			try {
				if(!(password1.getText().equals(password2.getText()))) {
					PopupScreen.error("Both password must be the same");
				}
				else {
					if(controller.isUsernameAvailable(username.getText())==false) {
						PopupScreen.error("Username is already taken");
					}
					else {
						//controller.registerUser(new User(username.getText(),password1.getText()));
					}
				}
			}
			catch(SQLException | IOException e2) {
				e2.printStackTrace();
			}
		});
	}

}
