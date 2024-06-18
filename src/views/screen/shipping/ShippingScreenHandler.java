package views.screen.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import controller.PlaceOrderController;
import common.exception.InvalidDeliveryInfoException;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderDTO;
import entity.order.OrderMedia;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {

	@FXML
	private Label screenTitle;

	@FXML
	private TextField name;

	@FXML
	private TextField email;
	
	@FXML
	private TextField phone;

	@FXML
	private TextField address;

	@FXML
	private TextField instructions;

	@FXML
	private ComboBox<String> province;

	private List<OrderMedia> productsInCart;

	public ShippingScreenHandler(Stage stage, String screenPath, List<OrderMedia> productsInCart) throws IOException {
		super(stage, screenPath);
		this.productsInCart= productsInCart;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		this.province.getItems().addAll(Configs.PROVINCES);
	}

	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

		// add info to messages
		HashMap messages = new HashMap<>();
		messages.put("name", name.getText());
		messages.put("email", email.getText());
		messages.put("phone", phone.getText());
		messages.put("address", address.getText());
		messages.put("instructions", instructions.getText());
		messages.put("province", province.getValue());
		var placeOrderCtrl = getBController();
		if (!placeOrderCtrl.validateContainLetterAndNoEmpty(name.getText())) {
            PopupScreen.error("Name is not valid!");
            return;
        }
		if (!placeOrderCtrl.validateEmail(email.getText())) {
            PopupScreen.error("Email is not valid!");
            return;
        }
        if (!placeOrderCtrl.validatePhoneNumber(phone.getText())) {
            PopupScreen.error("Phone is not valid!");
            return;

        }
        if (!placeOrderCtrl.validateContainLetterAndNoEmpty(address.getText())) {
            PopupScreen.error("Address is not valid!");
            return;
        }
        if (province.getValue() == null) {
            PopupScreen.error("Province is empty!");
            return;
        }
		try {
			// process and validate delivery info
			getBController().processDeliveryInfo(messages);
		} catch (InvalidDeliveryInfoException e) {
			throw new InvalidDeliveryInfoException(e.getMessage());
		}
	
		// calculate shipping fees
		int shippingFees = getBController().calculateShippingFee(productsInCart) + getBController().calculateItemsValue(productsInCart);
		//order.setShippingFees(shippingFees);
		//order.setDeliveryInfo(messages);
		//TODO FIX DATE:
		Order orderInfo = new Order(email.getText(), province.getValue(), address.getText(), phone.getText(), 0, shippingFees, 0,"Waiting Approval",name.getText(), Date.valueOf("2024-06-13"),instructions.getText());
		getBController().createOrder(orderInfo,productsInCart);
		OrderDTO order = new OrderDTO(orderInfo);
		order.setOrderMediaList(productsInCart);
		
		// create invoice screen
		Invoice invoice = getBController().createInvoice(order);
		
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}

	public void notifyError(){
		// TODO: implement later on if we need
	}
	@FXML 
	void goBack(MouseEvent event) {
		prev.show();
	}

}
