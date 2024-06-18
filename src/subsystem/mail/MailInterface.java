package subsystem.mail;

import entity.invoice.Invoice;

public interface MailInterface {
	void sendInvoiceEmail(Invoice invoice);
}
