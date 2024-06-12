package views.screen.admin;

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
	private TextField mediaTitle;
	@FXML
	private TextField mediaPrice;
	@FXML
	private Spinner mediaQuantity;
	@FXML
	private ImageView mediaImage;
	
	@FXML
	private Button deleteMedia;

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());
    private Media media;
    private AdminScreenHandler home;
    private Timeline holdTimeline;
    public MediaHandler(String screenPath, Media media, AdminScreenHandler home) throws SQLException, IOException{
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
        mediaQuantity.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, media.getQuantity())
        );

        setImage(mediaImage, media.getImageURL());
        
        
        
    }
    
}
