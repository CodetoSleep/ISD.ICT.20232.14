package views.screen.login;

import java.io.IOException;
import java.sql.SQLException;

import controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.productmanager.EditProductScreenHandler;

public class LoginScreenHandler extends BaseScreenHandler {
	
	@FXML
	private Label returnHome;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Button signIn;
	
	@FXML
	private Label signUp;
	
	private static LoginController controller;
	

	public LoginScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
		controller = new LoginController();
		returnHome.setOnMouseClicked(e->{
			homeScreenHandler.show();
		});
		
		signIn.setOnMouseClicked(e->{
//			if(username.getText().equals("a") && password.getText().equals("a")) {
//				homeScreenHandler.show();
//			}
//			if(username.getText().equals("") && password.getText().equals("")) {
//				this.editProductScreenHandler.show();
//			}
			try {
				user = controller.login(username.getText(),password.getText());
				if(user==null)PopupScreen.error("Login failed, wrong username or password");
				else {
					//add fix here when user update
					homeScreenHandler.show();
				}
			} catch (SQLException | IOException e1) {
				e1.printStackTrace();
			}
		});
		
		signUp.setOnMouseClicked(e->{
			SignInHandler signIn;
			try {
				signIn = new SignInHandler(this.stage,"/views/fxml/signin.fxml");
				signIn.setPreviousScreen(this);
				signIn.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
	}
	

}
