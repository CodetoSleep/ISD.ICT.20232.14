package views.screen.login;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;
import views.screen.admin.AdminScreenHandler;

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
	

	public LoginScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
		
		
		returnHome.setOnMouseClicked(e->{
			homeScreenHandler.show();
		});
		
		signIn.setOnMouseClicked(e->{
			if(username.getText().equals("a") && password.getText().equals("a")) {
				homeScreenHandler.show();
			}
			if(username.getText().equals("") && password.getText().equals("")) {
				this.adminScreenHandler.show();
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
