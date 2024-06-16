package views.screen.admin;

import java.io.IOException;

import entity.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;

public class UserProfileScreenHandler extends BaseScreenHandler{
	
	@FXML
	private Label username;
	
	@FXML
	private Label password;
	
	@FXML
	private ChoiceBox role;
	
	@FXML
	private Button deleteUser;
	
	private User user;

	public UserProfileScreenHandler(Stage stage, String screenPath, User user) throws IOException {
		super(stage, screenPath);
		this.user = user;
		
		username.setText(user.getUsername());
		password.setText(user.getPassword());
        ObservableList<String> options = FXCollections.observableArrayList("user","manager");
        role.setItems(options);
        role.getSelectionModel().select(options.indexOf(user.getRoleName()));
        
		
	}
	public User getUser() {
		return this.user;
	}

}
