package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by 2CLearning on 12/30/2017.
 */

public class PayOption {
    private String name;
    private String shortName;
    private int maximumAmount;
    private boolean active;
    private String reason;
    private String logoUrl;

    public PayOption(){

    }

    public PayOption(String name, String shortName, int maximumAmount, boolean active, String reason, String logoUrl) {
        this.name = name;
        this.shortName = shortName;
        this.maximumAmount = maximumAmount;
        this.active = active;
        this.reason = reason;
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(int maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}

