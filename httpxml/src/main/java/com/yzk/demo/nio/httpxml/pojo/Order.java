package com.yzk.demo.nio.httpxml.pojo;

public class Order {
    private long orderNumber;
    private Customer customer;

    /** Billing address information. */
    private Shipping shipping;

    /**
     * Shipping address information. If missing, the billing address is also
     * used as the shipping address.
     * */
    private Address shipTo;

    private Float total;


}
