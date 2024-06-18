package controller;

import entity.invoice.Invoice;
import subsystem.mail.*;

public class MailController {
	private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendInvoiceEmail(Invoice invoice) {
        mailService.sendInvoiceEmail(invoice);
    }
}
