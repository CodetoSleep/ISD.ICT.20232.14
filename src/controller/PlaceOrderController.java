package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderDTO;
import entity.order.OrderMedia;
import utils.Configs;


/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public void createOrder(Order order, List<OrderMedia> orderMediaList) throws SQLException {
        int orderId = new Order().insertOrder(order.getEmail(),
                order.getCity(),
                order.getAddress(),
                order.getPhone(),
                order.getRushhOrder(),
                order.getShippingFee(),
                order.getIsPaid(),
                order.getStatus(),
                order.getName(),
                order.getTime(),
                order.getShippingInstruction());
        new OrderMedia().insertOrderMedia(orderMediaList,orderId);
    }

    public static List<OrderDTO> getAllOrders() throws SQLException{
        List<OrderDTO> result = new ArrayList<>();
        List<Order> orders = new Order().getAllOrder();
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO(order);
            List<OrderMedia> orderMediaList = new OrderMedia().getAllOrderMediaByOrderId(order.getId());
            orderDTO.setOrderMediaList(orderMediaList);
            result.add(orderDTO);
        }
        return result;
    }

//    public static void main(String[] args) throws SQLException {
////        int orderId = new Order().insertOrder("minh.pham@example.com", "Ho Chi Minh City", "456 Elm St", "0987654321", 0, 30, 1, "APPROVED");
////        List<OrderMedia> orderMediaList = new ArrayList<>();
////        OrderMedia orderMedia = new OrderMedia();
////        Media media1 = new Media().getMediaById(10);
////        Media media2 = new Media().getMediaById(11);
////        Media media3 = new Media().getMediaById(12);
////
////        orderMediaList.add(new OrderMedia(media1, orderId, 1));
////        orderMediaList.add(new OrderMedia(media2, orderId, 2));
////        orderMediaList.add(new OrderMedia(media3, orderId, 3));
////        new OrderMedia().insertOrderMedia(orderMediaList);
//
//        List<OrderDTO> orderDTOS = getAllOrders();
//        for (OrderDTO orderDTO : orderDTOS) {
//            System.out.println(orderDTO);
//        }
//    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(OrderDTO order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }

    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{

    }

    public boolean validatePhoneNumber(String phoneNumber) {
    	if (phoneNumber.length() != 10)
            return false;
        if (Character.compare(phoneNumber.charAt(0), '0') != 0)
            return false;
        try {
            Long.parseUnsignedLong(phoneNumber);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
    
    public boolean validateContainLetterAndNoEmpty(String name) {
        // Check name is not null
        if (name == null)
            return false;
        // Check if contain letter space only
        if (name.trim().length() == 0)
            return false;
        // Check if contain only letter and space
        if (name.matches("^[a-zA-Z ]*$") == false)
            return false;
        return true;
    }
    
    public boolean validateEmail(String email) {
        // Check if email is not null
        if (email == null)
            return false;
        // Check if email contains spaces
        if (email.contains(" "))
            return false;
        // Check if email follows the basic pattern (something@domain)
        String emailPattern = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailPattern))
            return false;
        return true;
    }
    
    public boolean validateAddress(String address) {
    	// TODO: your work
    	return false;
    }
    
    //need to improve this
    public int calculateShippingFee(List<OrderMedia> order){
        int itemPrice = calculateItemsValue(order);
        
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * itemPrice );
        LOGGER.info("Order Amount: " + itemPrice + " -- Shipping Fees: " + fees);
        return fees;
    }
    public int calculateItemsValue(List<OrderMedia> order) {
    	double amount = 0;
        for (Object object : order) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getMedia().getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }
    static public List<OrderMedia> convertCartMediaToOrderMedia(List<CartMedia> productsList){
        List<OrderMedia> order = new ArrayList<OrderMedia>();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia().getId(), 
                                                   cartMedia.getMedia(), 
                                                   cartMedia.getQuantity());    
            order.add(orderMedia);
        }
        return order;
    	
    }

}
