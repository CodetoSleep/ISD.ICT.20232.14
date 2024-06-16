package views.screen;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import controller.BaseController;
import entity.user.User;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.screen.admin.AdminScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.productmanager.EditProductScreenHandler;

public class BaseScreenHandler extends FXMLScreenHandler {

	private Scene scene;
	private String cssPath;
	protected BaseScreenHandler prev;
	protected final Stage stage;
	static protected HomeScreenHandler homeScreenHandler;
	static protected EditProductScreenHandler editProductScreenHandler;
	static protected AdminScreenHandler adminScreenHandler;
	protected Hashtable<String, String> messages;
	private BaseController bController;
	static protected User user;
   
	private BaseScreenHandler(String screenPath) throws IOException {
		super(screenPath);
		this.stage = new Stage();
	}

	

	public void setPreviousScreen(BaseScreenHandler prev) {
		this.prev = prev;
	}

	public BaseScreenHandler getPreviousScreen() {
		return this.prev;
	}

	public BaseScreenHandler(Stage stage, String screenPath) throws IOException {
		super(screenPath);
		this.stage = stage;
	}
	public BaseScreenHandler(Stage stage, String screenPath,String cssPath) throws IOException {
		super(screenPath);
		this.cssPath = cssPath;
		this.stage = stage;
	}

	public void show() {
		if (this.scene == null) {
			this.scene = new Scene(this.content);
		}
		if(cssPath!= null) {
			this.scene.getStylesheets().add(getClass().getResource("cssPath").toExternalForm());
		}
		this.stage.setScene(this.scene);
		this.stage.show();
	}

	public void setScreenTitle(String string) {
		this.stage.setTitle(string);
	}

	public void setBController(BaseController bController){
		this.bController = bController;
	}

	public BaseController getBController(){
		return this.bController;
	}

	public void forward(Hashtable messages) {
		this.messages = messages;
	}

	public void setHomeScreenHandler(HomeScreenHandler HomeScreenHandler) {
		this.homeScreenHandler = HomeScreenHandler;
	}
	public void setManagerScreenHanler(EditProductScreenHandler adminScreen) {
		this.editProductScreenHandler = adminScreen;
	}
	public void setAdminScreenHandler(AdminScreenHandler adminScreen) {
		this.adminScreenHandler = adminScreen;
	}
	
	public void updateAccountAll() {
		homeScreenHandler.updateAccount();
		editProductScreenHandler.updateAccount();
		adminScreenHandler.updateAccount();
	}
	public void updateHomeItemsAll() {
		homeScreenHandler.updateHome();
		editProductScreenHandler.updateHomeItems();
		adminScreenHandler.updateHomeItems();
	}
	
}
