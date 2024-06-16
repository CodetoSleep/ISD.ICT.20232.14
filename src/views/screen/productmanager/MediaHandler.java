package views.screen.productmanager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.popup.PopupScreen;

public class MediaHandler extends FXMLScreenHandler{

	@FXML
	private Label mediaTitle;
	@FXML
	private Label mediaPrice;
	@FXML
	private Label mediaQuantity;
	@FXML
	private ImageView mediaImage;
	
	@FXML
	private ImageView editMedia;
	
	@FXML
	private Button deleteMedia;

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());
    private Media media;
    private EditProductScreenHandler home;
    private Timeline holdTimeline;
    public MediaHandler(String screenPath, Media media, EditProductScreenHandler home) throws SQLException, IOException{
        super(screenPath);
        this.media = media;
        this.home = home;
        setMediaInfo();
        
        deleteMedia.setOnMousePressed(e->{
            holdTimeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            	//set delete product here
                System.out.println("Button held for 3 seconds!");
            }));
            holdTimeline.setCycleCount(1);
            holdTimeline.play();
        });
        deleteMedia.setOnMouseReleased(e->{
        	if(holdTimeline!=null)holdTimeline.stop();
        });
        
        editMedia.setOnMouseClicked(e->{
        	try {
				ProductEditHandler.editProduct(this.media);
			} catch (IOException | SQLException e1) {
				e1.printStackTrace();
			}
        });
        
        
        
    }

    public Media getMedia(){
        return media;
    }

    private void setMediaInfo() throws SQLException {
        // set the cover image of media
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(110);
        mediaImage.setFitWidth(110);
        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
       
        mediaQuantity.setText(String.valueOf(media.getQuantity()));
        setImage(mediaImage, media.getImageURL());
        
        
        
    }
    
}
