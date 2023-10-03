package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class UnsentSms {
    private String id;
    private String sms_type;
    private String number;
    private String sms;

    public UnsentSms() {

    }

    public UnsentSms(String id, String sms_type, String number, String sms) {
        this.id = id;
        this.sms_type = sms_type;
        this.number = number;
        this.sms = sms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
