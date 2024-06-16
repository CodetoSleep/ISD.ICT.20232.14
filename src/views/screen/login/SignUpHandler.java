package views.screen.login;

import java.io.IOException;
import java.sql.SQLException;

import common.exception.InsufficientInputException;
import common.exception.UsernameAlreadyExistsException;
import controller.LoginController;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

public class SignUpHandler extends BaseScreenHandler{

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

	public SignUpHandler(Stage stage, String screenPath) throws IOException {
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
					try {
						controller.registerUser(new User(username.getText(),password1.getText()));
						adminScreenHandler.updateHomeItems();
						prev.show();
					}
					catch(InsufficientInputException e1) {
						PopupScreen.error("Invalid username or password");
					}
					catch(UsernameAlreadyExistsException e2) {
						PopupScreen.error("Username already exist");
					}

				}
			}
			catch(SQLException | IOException e2) {
				e2.printStackTrace();
			}

		});
	}

}
