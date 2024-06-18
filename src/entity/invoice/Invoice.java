package entity.invoice;

import entity.order.Order;
import entity.order.OrderDTO;

public class Invoice {

    private OrderDTO order;
    private int amount;
    
    public Invoice(){

    }

    public Invoice(OrderDTO order){
        this.order = order;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void saveInvoice(){
        
    }
}
