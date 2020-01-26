package com.yzk.demo.nio.marshalling.model;

import java.io.Serializable;
import java.util.List;

public class SubReq implements Serializable {
    private String reqId;
    private String userName;
    private String productName;
    private List<String> address;

    public SubReq() {
    }

    public SubReq(String reqId, String userName, String productName, List<String> address) {
        this.reqId = reqId;
        this.userName = userName;
        this.productName = productName;
        this.address = address;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SubReq{" +
                "reqId='" + reqId + '\'' +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", address=" + address +
                '}';
    }
}
