package views.screen.payment;

import controller.MailController;
import controller.PaymentController;
import entity.invoice.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import utils.Configs;
import utils.MailConfig;
import utils.VnPayConfig;
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import subsystem.mail.*;

public class PaymentScreenHandler extends BaseScreenHandler {

    private Invoice invoice;
    @FXML
    private Label pageTitle;
    @FXML
    private VBox vBox;


    public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;
        displayWebView();
    }
    
    
    private void displayWebView(){
        var paymentController = new PaymentController();
        var paymentUrl = paymentController.getUrlPay(invoice.getAmount(), "Thanh toan hoa don AIMS");
        WebView paymentView = new WebView();
        WebEngine webEngine = paymentView.getEngine();
        webEngine.load(paymentUrl);
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            // Xử lý khi URL thay đổi
            handleUrlChanged(newValue);
        });
        vBox.getChildren().clear();
        vBox.getChildren().add(paymentView);
    }

    // Hàm chuyển đổi query string thành Map
    private static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    /**
     * @param newValue url vnPay return về
     */
    private void handleUrlChanged(String newValue) {
        if (newValue.contains(VnPayConfig.vnp_ReturnUrl)) {
            try {
                URI uri = new URI(newValue);
                String query = uri.getQuery();

                // Chuyển đổi query thành Map
                Map<String, String> params = parseQueryString(query);

                payOrder(params);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param res kết quả vnPay trả về
     * @throws IOException
     */
    void payOrder(Map<String, String> res) throws IOException {

        var ctrl = (PaymentController) super.getBController();
        Map<String, String> response = ctrl.makePayment(res, this.invoice.getOrder().getId(), invoice);

        BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH,
                response.get("RESULT"), response.get("MESSAGE"));
        ctrl.emptyCart();
        resultScreen.setPreviousScreen(this);
        resultScreen.setHomeScreenHandler(homeScreenHandler);
        resultScreen.setScreenTitle("Result Screen");
        resultScreen.show();
    }
    

    public void SendEmail(Invoice invoice) {
    	Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        try {
            Transport transport = session.getTransport("smtp");
            transport.connect("minhz123456@gmail.com", "jycyhkltlwfjrgqo");

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("minhz123456@gmail.com"));
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress((String) invoice.getOrder().getEmail()));

            message.setSubject("AIMS GROUP-14 NOTIFICATION");

            String content = "AIMS GROUP-14 NOTIFICATION\n\n" +
                "Your order has been placed successfully. Here is your invoice's information. Please check it.\n" +
                "Order ID: " + invoice.getOrder().getId() + " (You can later use it to review your order in app)\n" +
                "Recipient's name: " + invoice.getOrder().getName() + "\n" +
                "Phone number: " + invoice.getOrder().getPhone() + "\n" +
                "Address: " + invoice.getOrder().getAddress() + "\n" +
                "Email: " + invoice.getOrder().getEmail() + "\n" +
                "Total amount: " + invoice.getAmount() * 1000 + "\n" +
                "Your order will be processed by manager later.\nPlease check your mail regularly.\n\nThank you.";

            message.setText(content);

            transport.sendMessage(message, message.getAllRecipients());

            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}