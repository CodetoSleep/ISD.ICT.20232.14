package subsystem;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	public static void main(String[] args) {
		Properties props;
		Session session;
		Transport transport;
		MimeMessage message;
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		session = Session.getDefaultInstance(props);

		try {
			transport = session.getTransport("smtp");
			transport.connect("minhz123456@gmail.com", "jycyhkltlwfjrgqo");

			InternetAddress[] recipients = new InternetAddress[3];
			recipients[0] = new InternetAddress("currycaugiay123@gmail.com");
			recipients[1] = new InternetAddress("namgt283@gmail.com");
			recipients[2] = new InternetAddress("minh.nc215224@sis.hust.edu.vn");
			//recipients[3] = new InternetAddress("recipient3@somedomain.ru");
			//recipients[4] = new InternetAddress("recipient4@163.com");
			// ...

			message = new MimeMessage(session);
			message.setFrom(new InternetAddress("minhz123456@gmail.com")); 
			message.setSubject("The First Test Email");
			message.setText("This is the first test email. ");

			transport.sendMessage(message, recipients);

			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}