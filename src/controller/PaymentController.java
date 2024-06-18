package controller;

import common.exception.PaymentException;
//import common.exception.TransactionNotDoneException;
import common.exception.UnrecognizedException;
import entity.cart.Cart;
import entity.invoice.Invoice;
import subsystem.mail.MailService;
import subsystem.vnpay.VnPayInterface;
import subsystem.vnpay.VnPaySubsystem;
import utils.MailConfig;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Map;

/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 */
public class PaymentController extends BaseController {


    /**
     * Represent the Interbank subsystem
     */
    private VnPayInterface vnPayService;
    private Invoice invoice;

    /**
     * Validate the input date which should be in the format "mm/yy", and then
     * return a {@link java.lang.String String} representing the date in the
     * required format "mmyy" .
     *
     * @param date - the {@link java.lang.String String} represents the input date
     * @return {@link java.lang.String String} - date representation of the required
     * format
     * @throws TransactionNotDoneException - if the string does not represent a valid date
     *                                     in the expected format
     */

    public Map<String, String> makePayment(Map<String, String> res, int orderId, Invoice invoice) {
        Map<String, String> result = new Hashtable<String, String>();
        try {
        	this.invoice = invoice;
        	MailConfig mailConfig = new MailConfig(
                    "smtp.gmail.com",
                    "465",
                    "true",
                    "javax.net.ssl.SSLSocketFactory",
                    "minhz123456@gmail.com",
                    "jycyhkltlwfjrgqo"
                );
            MailService mailService = new MailService(mailConfig);
            MailController mailController = new MailController(mailService);

            // Assume Invoice is properly instantiated with the required details.
            mailController.sendInvoiceEmail(invoice);
            this.vnPayService = new VnPaySubsystem();
            var trans = vnPayService.makePaymentTransaction(res);
            trans.save(invoice.getOrder().getId());
            result.put("RESULT", "PAYMENT SUCCESSFUL!");
            result.put("MESSAGE", "You have succesffully paid the order!");
        } catch (PaymentException | UnrecognizedException | SQLException ex) {
            result.put("MESSAGE", ex.getMessage());
            result.put("RESULT", "PAYMENT FAILED!");

        } catch (ParseException ex) {
            result.put("MESSAGE", ex.getMessage());
            result.put("RESULT", "PAYMENT FAILED!");
        }
        return result;
    }

    /**
     * Gen url thanh toán vnPay
     * @param amount
     * @param content
     * @return
     */
    public String getUrlPay(int amount, String content){
        vnPayService = new VnPaySubsystem();
        var url = vnPayService.generatePayUrl(amount, content);
        return url;
    }

    public void emptyCart() {
        Cart.getCart().emptyCart();
    }
}