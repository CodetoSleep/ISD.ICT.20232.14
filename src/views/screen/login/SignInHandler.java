package views.screen.login;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;

public class SignInHandler extends BaseScreenHandler{
	
	@FXML
	private Button signIn;

	public SignInHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
		
		signIn.setOnMousePressed(e->{
			prev.show();
		});
	}

}
