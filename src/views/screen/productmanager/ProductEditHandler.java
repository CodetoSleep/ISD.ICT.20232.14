package views.screen.productmanager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import controller.MediaController;
import entity.media.Media;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;


public class ProductEditHandler extends BaseScreenHandler{
	
	@FXML
	private Button publishProduct;
	
	@FXML
	private TextField title;
	
	@FXML
	private TextField sellingPrice;
	
	@FXML
	private TextField value;
	
	@FXML
	private TextField category;
	
	@FXML
	private TextField quantity;
	
	@FXML
	private ChoiceBox type;
	
	@FXML
	private CheckBox rushOrder;
	
	@FXML
	private ImageView image;
	
	@FXML
	private Button editImage;
	
    private MediaController controller;
    private String imageUrl;
    
    public ProductEditHandler(Stage stage) throws IOException{
    	
        super(stage, "/views/fxml/editmedia.fxml");
        controller = new MediaController();
        publishProduct.setOnMouseClicked(e->{
        	try {
				controller.insertMedia(title.getText(),category.getText(),Integer.parseInt(sellingPrice.getText()),Integer.parseInt(value.getText()),Integer.parseInt(quantity.getText()),(String) type.getValue(),imageUrl,rushOrder.isSelected()?1:0);
			} catch (NumberFormatException | SQLException e1) {
				try {
					PopupScreen.error("Insert Failed");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
			}
        	updateHomeItemsAll();
        	stage.close();
        });
        ObservableList<String> options = FXCollections.observableArrayList("cd", "dvd", "book");
        type.setItems(options);
        type.getSelectionModel().select(0);
        editImage.setOnMouseClicked(e->{
        	selectImage();
        });
        
        
        
    }
    public ProductEditHandler(Stage stage, Media media) throws IOException, SQLException{
        this(stage);
        
        
        title.setText(media.getTitle());
        quantity.setText(String.valueOf(media.getQuantity()));
        category.setText(media.getCategory());
        sellingPrice.setText(String.valueOf(media.getPrice()));
        value.setText(String.valueOf(media.getValue()));
        //value.setText(media.getImageURL());
        ObservableList<String> options = FXCollections.observableArrayList("cd", "dvd", "book");
        type.getSelectionModel().select(options.indexOf(media.getType()));
        rushOrder.setSelected(media.getRushOrder()>0);
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        this.image.setImage(image);
        imageUrl = media.getImageURL();
        
        publishProduct.setOnMouseClicked(e->{
        	try {
        		Media me = new Media(media.getId(),title.getText(),category.getText(),Integer.parseInt(sellingPrice.getText()),Integer.parseInt(value.getText()),Integer.parseInt(quantity.getText()),(String) type.getValue(),imageUrl,rushOrder.isSelected()?1:0);
				controller.updateMediaById(media.getId(), me);
        		updateHomeItemsAll();
        		stage.close();
			} catch (NumberFormatException | SQLException e1) {
				try {
					PopupScreen.error("Edit failed");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
			}
        });
    }


    public static void editProduct(Media media) throws IOException, SQLException{
    	ProductEditHandler popup = new ProductEditHandler(new Stage(),media);
        
    	
    	
        popup.show();
    }

    public static void createProduct() throws IOException{
    	ProductEditHandler popup = new ProductEditHandler(new Stage());
        
        popup.show();
    }
    
    
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("assets/images");
        fileChooser.setInitialDirectory(initialDirectory);
        File selectedFile = fileChooser.showOpenDialog(image.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            this.image.setImage(image);
            String fileName = selectedFile.getName();
            File parentDirectory = selectedFile.getParentFile();

            imageUrl = "assets/images/" + parentDirectory.getName() + "/" + fileName;
        }
    }



    
}
