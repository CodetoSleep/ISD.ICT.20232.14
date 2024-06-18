package subsystem.mail;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import entity.invoice.Invoice;
import utils.MailConfig;
public class MailService implements MailInterface{
	private final MailConfig mailConfig;

    public MailService(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    @Override
    public void sendInvoiceEmail(Invoice invoice) {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfig.getSmtpHost());
        props.put("mail.smtp.port", mailConfig.getSmtpPort());
        props.put("mail.smtp.socketFactory.class", mailConfig.getSmtpSocketFactoryClass());
        props.put("mail.smtp.auth", mailConfig.getSmtpAuth());

        Session session = Session.getDefaultInstance(props);

        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(mailConfig.getUsername(), mailConfig.getPassword());

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getUsername()));
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
