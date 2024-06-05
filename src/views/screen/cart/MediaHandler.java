package views.screen.cart;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

import common.exception.MediaUpdateException;
import common.exception.ViewCartException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import utils.Configs;
import utils.Utils;
import views.screen.FXMLScreenHandler;

public class MediaHandler extends FXMLScreenHandler {

	private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());

	@FXML
	protected HBox hboxMedia;

	@FXML
	protected ImageView image;

	@FXML
	protected VBox description;

	@FXML
	protected Label labelOutOfStock;
	
	@FXML
	protected ImageView availabilityCheckmark;

	@FXML
	protected HBox spinnerFX2;
	
	@FXML
	protected Button decreaseMediaCart;
	
	@FXML
	protected Button increaseMediaCart;
	
	@FXML
	protected Label title;

	@FXML
	protected Label price;

	@FXML
	protected Label currency;

	@FXML
	protected ImageView btnDelete;

	private CartMedia cartMedia;

	private TextField spinner2;
	private Image greentick;
	private Image redtick;
	private CartScreenHandler cartScreen;

	public MediaHandler(String screenPath, CartScreenHandler cartScreen) throws IOException {
		super(screenPath);
		this.cartScreen = cartScreen;
		hboxMedia.setAlignment(Pos.CENTER);
		
		//set tick images
		File file = new File("assets/images/tickerror.png");
        this.redtick = new Image(file.toURI().toString());
        file = new File("assets/images/tickgreen.png");
        this.greentick = new Image(file.toURI().toString());
        
	}
	
	public void setCartMedia(CartMedia cartMedia) {
		this.cartMedia = cartMedia;
		setMediaInfo();
	}

	private void setMediaInfo() {
		title.setText(cartMedia.getMedia().getTitle());
		price.setText(Utils.getCurrencyFormat(cartMedia.getPrice()));
		File file = new File(cartMedia.getMedia().getImageURL());
		Image im = new Image(file.toURI().toString());
		image.setImage(im);
		image.setPreserveRatio(false);
		image.setFitHeight(110);
		image.setFitWidth(92);

		// add delete button
		//btnDelete.setFont(Configs.REGULAR_FONT);
		btnDelete.setOnMouseClicked(e -> {
			try {
				Cart.getCart().removeCartMedia(cartMedia); // update user cart
				cartScreen.updateCart(); // re-display user cart
				LOGGER.info("Deleted " + cartMedia.getMedia().getTitle() + " from the cart");
			} catch (SQLException exp) {
				exp.printStackTrace();
				throw new ViewCartException();
			}
		});
		initializeSpinner2();
	}

	
	private void initializeSpinner2(){
		spinner2= new TextField();
		spinner2.setText(String.valueOf(cartMedia.getQuantity()));
		this.increaseMediaCart.setOnMouseClicked(e ->{
			int numOfProd = Integer.parseInt(spinner2.getText());
			numOfProd++;
			spinner2.setText(String.valueOf(numOfProd));
			updateSpinner(spinner2);
		});
		this.decreaseMediaCart.setOnMouseClicked(e ->{
			int numOfProd = Integer.parseInt(spinner2.getText());
			numOfProd--;
			if(numOfProd >=0){
				spinner2.setText(String.valueOf(numOfProd));
				updateSpinner(spinner2);
			}
		});
		spinnerFX2.getChildren().add(1,this.spinner2);
		
	}
	private void updateSpinner(TextField spinner) {
		try {
			int numOfProd = Integer.parseInt(spinner.getText());
			int remainQuantity = cartMedia.getMedia().getQuantity();
			LOGGER.info("NumOfProd: " + numOfProd + " -- remainOfProd: " + remainQuantity);
			if (numOfProd > remainQuantity){
				LOGGER.info("product " + cartMedia.getMedia().getTitle() + " only remains " + remainQuantity + " (required " + numOfProd + ")");
				labelOutOfStock.setText("Sorry, Only " + remainQuantity + " remain in stock");
				availabilityCheckmark.setImage(redtick);
				labelOutOfStock.setTextFill(Color.RED);
				spinner.setText(String.valueOf(remainQuantity));
				numOfProd = remainQuantity;
			}
			if(numOfProd<remainQuantity) {
				availabilityCheckmark.setImage(greentick);
				//labelOutOfStock.setText(remainQuantity + " remain in stock");
				labelOutOfStock.setTextFill(Color.GREEN);
			}

			// update quantity of mediaCart in useCart
			cartMedia.setQuantity(numOfProd);

			// update the total of mediaCart
			price.setText(Utils.getCurrencyFormat(numOfProd*cartMedia.getPrice()));

			// update subtotal and amount of Cart
			cartScreen.updateCartAmount();
		}
		catch (SQLException e1) {
			throw new MediaUpdateException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
		}
	
	}
}