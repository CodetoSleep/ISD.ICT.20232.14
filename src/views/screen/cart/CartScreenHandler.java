package views.screen.cart;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.ViewCartController;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.Order;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

public class CartScreenHandler extends BaseScreenHandler {

	private static Logger LOGGER = Utils.getLogger(CartScreenHandler.class.getName());

	@FXML
	private HBox homeScreen;

	@FXML
	private Label pageTitle;

	@FXML
	VBox vboxCart;
	
	@FXML
	private Label homeBtn;

	@FXML
	private Label shippingFees;

	@FXML
	private Label labelAmount;

	@FXML
	private Label labelSubtotal;

	@FXML
	private Label labelVAT;

	@FXML
	private Button btnPlaceOrder;

	public CartScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);

		

		// on mouse clicked, we back to home
		homeScreen.setOnMouseClicked(e -> {
			homeScreenHandler.show();
		});
		homeBtn.setOnMouseClicked(e -> {
			homeScreenHandler.show();
		});

		// on mouse clicked, we start processing place order usecase
		btnPlaceOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Order button clicked");
			try {
				requestToPlaceOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}
			
		});
	}

	public Label getLabelAmount() {
		return labelAmount;
	}

	public Label getLabelSubtotal() {
		return labelSubtotal;
	}

	public ViewCartController getBController(){
		return (ViewCartController) super.getBController();
	}

	public void requestToViewCart(BaseScreenHandler prevScreen) throws SQLException {
		setPreviousScreen(prevScreen);
		setScreenTitle("Cart Screen");
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
		show();
	}

	public void requestToPlaceOrder() throws SQLException, IOException {
		try {
			// create placeOrderController and process the order
			PlaceOrderController placeOrderController = new PlaceOrderController();
			if (placeOrderController.getListCartMedia().size() == 0){
				PopupScreen.error("You don't have anything to place");
				return;
			}
			
			placeOrderController.placeOrder();
			
			
			// display available media
			displayCartWithMediaAvailability();

			// create order
			List<OrderMedia> productsInCart = placeOrderController.convertCartMediaToOrderMedia(Cart.getCart().getListMedia());

			// display shipping form
			ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, productsInCart);
			ShippingScreenHandler.setPreviousScreen(this);
			ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
			ShippingScreenHandler.setScreenTitle("Shipping Screen");
			ShippingScreenHandler.setBController(placeOrderController);
			ShippingScreenHandler.show();

		} catch (MediaNotAvailableException e) {
			// if some media are not available then display cart and break usecase Place Order
			PopupScreen.error(e.getMessage());
			displayCartWithMediaAvailability();
		}
	}

	public void updateCart() throws SQLException{
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
	}

	void updateCartAmount(){
		// calculate subtotal and amount
		int subtotal = getBController().getCartSubtotal();
		int vat = (int)((Configs.PERCENT_VAT/100)*subtotal);
		int amount = subtotal + vat;
		LOGGER.info("amount" + amount);

		// update subtotal and amount of Cart
		labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
		labelVAT.setText(Utils.getCurrencyFormat(vat));
		labelAmount.setText(Utils.getCurrencyFormat(amount));
	}
	
	private void displayCartWithMediaAvailability(){
		// clear all old cartMedia
		vboxCart.getChildren().clear();

		// get list media of cart after check availability
		List lstMedia = getBController().getListCartMedia();

		try {
			for (Object cm : lstMedia) {

				// display the attribute of vboxCart media
				CartMedia cartMedia = (CartMedia) cm;
				MediaHandler mediaCartScreen = new MediaHandler(Configs.CART_MEDIA_PATH, this);
				mediaCartScreen.setCartMedia(cartMedia);
			

				// add spinner
				vboxCart.getChildren().add(mediaCartScreen.getContent());
				
				//adding somespace ...
				VBox innerVBox = new VBox();
		        innerVBox.setPrefHeight(10);
		        vboxCart.getChildren().add(innerVBox);
				
			}
			// calculate subtotal and amount
			updateCartAmount();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}