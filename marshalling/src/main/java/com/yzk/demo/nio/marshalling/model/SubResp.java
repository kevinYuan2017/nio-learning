package com.yzk.demo.nio.marshalling.model;

import java.io.Serializable;

public class SubResp implements Serializable {
    private String reqId;
    private int respCode;
    private String desc;

    public SubResp(String reqId, int respCode, String desc) {
        this.reqId = reqId;
        this.respCode = respCode;
        this.desc = desc;
    }

    public SubResp() {
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubResp{" +
                "reqId='" + reqId + '\'' +
                ", respCode=" + respCode +
                ", desc='" + desc + '\'' +
                '}';
    }
}
