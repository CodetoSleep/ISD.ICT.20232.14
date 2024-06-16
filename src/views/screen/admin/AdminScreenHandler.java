package views.screen.admin;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import controller.UserController;

import entity.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import utils.Utils;
import views.screen.BaseScreenHandler;

import views.screen.login.LoginScreenHandler;


public class AdminScreenHandler extends BaseScreenHandler implements Initializable{

    public static Logger LOGGER = Utils.getLogger(AdminScreenHandler.class.getName());

    @FXML
    private HBox homeScreen;
    
    @FXML
    private HBox editUser;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;
    
    @FXML
    private Label welcomeText;
    
    @FXML
    private Label logIn;

    
    @FXML
    private Button search;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button forward;
    
    @FXML
    private Button backward;
    
    @FXML
    private ChoiceBox sortByCategory;

    private List homeItems;
    
    
    private int currentPage = 0;
    private List currentFilteredItems;
    private UserController controller ;

    public AdminScreenHandler(Stage stage, String screenPath) throws IOException{
        super(stage, screenPath);
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	controller = new UserController();
        updateHomeItems();
        
        editUser.setOnMouseClicked(e -> {
        	currentPage = 0;
        	currentFilteredItems = homeItems;
        	sortByCategory.getSelectionModel().select(0);
            addMediaHome(this.homeItems);
            updateHomeItems();
        });
        
        homeScreen.setOnMouseClicked(e->{
        	homeScreenHandler.show();
        });
        
        logIn.setOnMouseClicked(e->{
        	LoginScreenHandler loginScreen;
        	try {
        		loginScreen = new LoginScreenHandler(this.stage,"/views/fxml/login.fxml");
        		if(user!=null)user=null;
        		updateAccountAll();
        		loginScreen.show();
        	}
        	catch (IOException e1) {
        		e1.printStackTrace();
        	}
        	
        });
        search.setOnMouseClicked(e->{
        	onSearchAction();
        });
        searchField.setOnAction(e->{
        	onSearchAction();
        });
        backward.setOnMouseClicked(e->{
        	moveItemPageBackward(currentFilteredItems);
        });
        forward.setOnMouseClicked(e->{
        	moveItemPageForward(currentFilteredItems);
        });
        
        ObservableList<String> options = FXCollections.observableArrayList("all", "user","manager");
        sortByCategory.setItems(options);
        sortByCategory.getSelectionModel().select(0);
        sortByCategory.setOnAction(e->{
        	currentPage = 0;
        	currentFilteredItems = getItemByCategory(searchItem(searchField.getText(),homeItems),sortByCategory.getValue().toString());
        	addMediaHome(currentFilteredItems);
        });
        currentFilteredItems = homeItems;
    }
    public void updateAccount() {
    	if(user!=null) {
    		welcomeText.setText("Welcome, " + user.getUsername());
    		logIn.setText("Sign out");
    	}
    	else {
    		welcomeText.setText("");
    		logIn.setText("Sign In");
    	}
    }
    public void updateHomeItems() {
    	updateAccount();
    	try{
            List medium = controller.getAllUsersWithRoles();
    		
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                User media = (User)object;
                if(!media.getRoleName().equals("admin")) {
                	UserProfileScreenHandler m1 = new UserProfileScreenHandler(this.stage,"/views/fxml/useredit_admin.fxml", media);
                	this.homeItems.add(m1);
                }
            }
            addMediaHome(homeItems);
        }catch (IOException | SQLException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void addMediaHome(List items){
        ArrayList mediaItems = (ArrayList)((ArrayList) items).clone();
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while(!mediaItems.isEmpty()){
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while(vBox.getChildren().size()<3 && !mediaItems.isEmpty()){
                	UserProfileScreenHandler media = (UserProfileScreenHandler) mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
        }
    }
    
    private List getItemByCategory(List items,String category) {
    	if(category.equals("all"))return items;
    	List filteredItems = new ArrayList<>();
    	items.forEach(me ->{
    		UserProfileScreenHandler media = (UserProfileScreenHandler) me;
        	if (media.getUser().getRoleName().toLowerCase().startsWith(category)){
                 filteredItems.add(media);
        	}
    	});
    	return filteredItems;
    }
    
    private List searchItem(String keyWord, List itemList) {
    	List filteredItems = new ArrayList<>();
    	itemList.forEach(me->{
    		UserProfileScreenHandler media = (UserProfileScreenHandler) me;
    		if(media.getUser().getUsername().toLowerCase().startsWith(keyWord.toLowerCase())){
    			filteredItems.add(media);
    		}
    	});
    	return filteredItems;
    }
    private void onSearchAction() {
    	currentFilteredItems = searchItem(searchField.getText(),getItemByCategory(homeItems,sortByCategory.getValue().toString()));
    	currentPage = 0;
    	addMediaHome(currentFilteredItems);
    }
    private void moveItemPageForward(List category) {
    	List filteredItems = new ArrayList<>();
    	if((currentPage+1)*9 < category.size()) {
    		this.currentPage++;
    		for(int i = currentPage*9; i<category.size();i++) {
    			filteredItems.add(category.get(i));
    		}
    		addMediaHome(filteredItems);
    	}
    	
    }
    private void moveItemPageBackward(List category) {
    	List filteredItems = new ArrayList<>();
    	if(currentPage*9 > 0) {
    		this.currentPage--;
    		for(int i = currentPage*9; i<category.size();i++) {
    			filteredItems.add(category.get(i));
    		}
    		addMediaHome(filteredItems);
    	}
    	
    }

    
    
}
