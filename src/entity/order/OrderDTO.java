package entity.order;

import java.sql.Date;
import java.util.List;

public class OrderDTO {
    private int id;
    private String email;
    private String city;
    private String address;
    private String phone;
    private int rushhOrder;
    private int shippingFee;
    private int isPaid;
    private String status;
    private String name;
    private Date time;
    private String shippingInstruction;
    List<OrderMedia> orderMediaList;

    public OrderDTO() {}

    public OrderDTO(String email, String city, String address, String phone, int rushhOrder, int shippingFee, int isPaid, String status, List<OrderMedia> orderMediaList) {
        this.email = email;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.rushhOrder = rushhOrder;
        this.shippingFee = shippingFee;
        this.isPaid = isPaid;
        this.status = status;
        this.orderMediaList = orderMediaList;
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.email = order.getEmail();
        this.city = order.getCity();
        this.address = order.getAddress();
        this.phone = order.getPhone();
        this.rushhOrder = order.getRushhOrder();
        this.shippingFee = order.getShippingFee();
        this.isPaid = order.getIsPaid();
        this.status = order.getStatus();
        this.name = order.getName();
        this.time = order.getTime();
        this.shippingInstruction = order.getShippingInstruction();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getShippingInstruction() {
        return shippingInstruction;
    }

    public void setShippingInstruction(String shippingInstruction) {
        this.shippingInstruction = shippingInstruction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRushhOrder() {
        return rushhOrder;
    }

    public void setRushhOrder(int rushhOrder) {
        this.rushhOrder = rushhOrder;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderMedia> getOrderMediaList() {
        return orderMediaList;
    }

    public void setOrderMediaList(List<OrderMedia> orderMediaList) {
        this.orderMediaList = orderMediaList;
    }
}
