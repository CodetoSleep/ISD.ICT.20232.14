package views.screen.productmanager;

import java.io.IOException;

import entity.media.Media;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.Configs;
import views.screen.BaseScreenHandler;


public class ProductEditHandler extends BaseScreenHandler{
	
	@FXML
	private Button publishProduct;
    
    
    public ProductEditHandler(Stage stage) throws IOException{
        super(stage, "/views/fxml/editmedia.fxml");
        publishProduct.setOnMouseClicked(e->{
        	stage.close();
        });
    }


    public static void editProduct(Media media) throws IOException{
    	ProductEditHandler popup = new ProductEditHandler(new Stage());
        //popup.stage.initStyle(StageStyle.UNDECORATED);
    	
        popup.show();
    }

    public static void createProduct() throws IOException{
    	ProductEditHandler popup = new ProductEditHandler(new Stage());
        //popup.stage.initStyle(StageStyle.UNDECORATED);
        popup.show();
    }



    
}
