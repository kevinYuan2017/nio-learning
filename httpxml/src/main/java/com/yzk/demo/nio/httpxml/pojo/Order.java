package com.yzk.demo.nio.httpxml.pojo;

public class Order {
    private long orderNumber;
    private Customer customer;

    /** Billing address information. */
    private Address billTo;
    private Shipping shipping;

    /**
     * Shipping address information. If missing, the billing address is also
     * used as the shipping address.
     * */
    private Address shipTo;

    private Float total;

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getBillTo() {
        return billTo;
    }

    public void setBillTo(Address billTo) {
        this.billTo = billTo;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Address getShipTo() {
        return shipTo;
    }

    public void setShipTo(Address shipTo) {
        this.shipTo = shipTo;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", customer=" + customer +
                ", billTo=" + billTo +
                ", shipping=" + shipping +
                ", shipTo=" + shipTo +
                ", total=" + total +
                '}';
    }

    public static Order create(long orderID) {
        Order order = new Order();

        order.setOrderNumber(orderID);
        order.setTotal(9999.999f);

        Address address = new Address();
        address.setCity("南京市");
        address.setCountry("中国");
        address.setPostCode("123321");
        address.setState("江苏省");
        address.setStreet1("龙眠大道");
        order.setBillTo(address);

        Customer customer = new Customer();
        customer.setCustomerNumber(orderID);
        customer.setFirstName("李");
        customer.setLastName("林峰");
        order.setCustomer(customer);

        order.setShipping(Shipping.INTERNATIONAL_MAIL);
        order.setShipTo(address);
        return order;
    }
}
