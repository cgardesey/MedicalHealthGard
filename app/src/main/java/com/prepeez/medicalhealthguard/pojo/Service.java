package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class Service {
    private String service;
    private boolean isMoblileService;

    public Service() {

    }

    public Service(String service, boolean isMoblileService) {
        this.service = service;
        this.isMoblileService = isMoblileService;
    }

    @Override
    public boolean equals(Object obj) {
        return this.service.equals(((Service) obj).getService());

    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isMoblileService() {
        return isMoblileService;
    }

    public void setMoblileService(boolean moblileService) {
        isMoblileService = moblileService;
    }
}
