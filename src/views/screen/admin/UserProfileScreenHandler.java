package views.screen.admin;

import java.io.IOException;
import java.sql.SQLException;

import controller.UserController;
import entity.user.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

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


	private UserController controller;

	public UserProfileScreenHandler(Stage stage, String screenPath, User user) throws IOException {
		super(stage, screenPath);
		this.user = user;
		controller = new UserController();
		username.setText(user.getUsername());
		password.setText(user.getPassword());
		ObservableList<String> options = FXCollections.observableArrayList("user","manager");
		role.setItems(options);
		role.getSelectionModel().select(options.indexOf(user.getRoleName()));

		deleteUser.setOnMousePressed(e->{
				try {
					controller.deleteUser(user.getUserId());
					adminScreenHandler.updateHomeItems();
				} catch (SQLException e1) {
					try {
						PopupScreen.error("Delete failed");
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					e1.printStackTrace();
				}
		});
		role.setOnAction(e->{
			try {
				controller.updateUser(user, String.valueOf(role.getValue()));
				adminScreenHandler.updateHomeItems();
			} catch (SQLException e1) {
				try {
					PopupScreen.error("Role changed failed");
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				e1.printStackTrace();
			}
			
		});
		

	}
	public User getUser() {
		return this.user;
	}

}
