package com.yzk.demo.nio.httpxml.pojo;

import java.util.List;

public class Customer {
    private long customerNumber;
    /** Personal name. */
    private String firstName;

    /** Family name. */
    private String lastName;

    /** Middle name. */
    private List<String> middleName;

    public long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getMiddleName() {
        return middleName;
    }

    public void setMiddleName(List<String> middleName) {
        this.middleName = middleName;
    }
}
