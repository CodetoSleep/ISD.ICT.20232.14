package views.screen.admin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import common.exception.ViewCartException;
import controller.BaseController;
import controller.HomeController;
import controller.ViewCartController;
import entity.cart.Cart;
import entity.media.Media;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.cart.CartScreenHandler;
import views.screen.login.LoginScreenHandler;


public class AdminScreenHandler extends BaseScreenHandler implements Initializable{

    public static Logger LOGGER = Utils.getLogger(AdminScreenHandler.class.getName());

    @FXML
    private Label numMediaInCart;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView adminLogin;
    
    @FXML
    private HBox viewCart;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;

    
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

    public AdminScreenHandler(Stage stage, String screenPath) throws IOException{
        super(stage, screenPath);
    }

    public Label getNumMediaCartLabel(){
        return this.numMediaInCart;
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());
        try{
            List medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                MediaHandler m1 = new MediaHandler("/views/fxml/media_admin.fxml", media, this);
                this.homeItems.add(m1);
            }
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }
            
        aimsImage.setOnMouseClicked(e -> {
        	currentPage = 0;
        	currentFilteredItems = homeItems;
        	sortByCategory.getSelectionModel().select(0);
            addMediaHome(this.homeItems);
        });
        
        
        adminLogin.setOnMouseClicked(e->{
        	LoginScreenHandler loginScreen;
        	try {
        		loginScreen = new LoginScreenHandler(this.stage,"/views/fxml/login.fxml");
        		loginScreen.setHomeScreenHandler(this.homeScreenHandler);
        		loginScreen.setAdminScreenHanler(this);
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
        
        ObservableList<String> options = FXCollections.observableArrayList("all", "cd", "dvd", "book");
        sortByCategory.setItems(options);
        sortByCategory.getSelectionModel().select(0);
        sortByCategory.setOnAction(e->{
        	currentPage = 0;
        	currentFilteredItems = getItemByCategory(searchItem(searchField.getText(),homeItems),sortByCategory.getValue().toString());
        	addMediaHome(currentFilteredItems);
        });
        
        addMediaHome(this.homeItems);
        currentFilteredItems = homeItems;
    }

    public void setImage(){
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo2.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);
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
                    MediaHandler media = (MediaHandler) mediaItems.get(0);
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
    		MediaHandler media = (MediaHandler) me;
        	if (media.getMedia().getType().toLowerCase().startsWith(category)){
                 filteredItems.add(media);
        	}
    	});
    	return filteredItems;
    }
    
    private List searchItem(String keyWord, List itemList) {
    	List filteredItems = new ArrayList<>();
    	itemList.forEach(me->{
    		MediaHandler media = (MediaHandler) me;
    		if(media.getMedia().getTitle().toLowerCase().startsWith(keyWord.toLowerCase())){
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
