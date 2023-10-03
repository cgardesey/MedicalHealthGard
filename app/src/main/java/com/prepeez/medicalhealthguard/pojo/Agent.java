package com.prepeez.medicalhealthguard.pojo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class Agent implements Serializable {
    @Expose
    private int id;
    @Expose
    private String agentid;
    @Expose
    private String zone;
    @Expose
    private String picture;
    @Expose
    private String title;
    @Expose
    private String firstname;
    @Expose
    private String lastname;
    @Expose
    private String othername;
    @Expose
    private String gender;
    @Expose
    private String dateofbirth;
    @Expose
    private String contact;
    @Expose
    private String address;
    @Expose
    private String dateofemployment;
    @Expose
    private int active;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;

    public Agent(){

    }

    public Agent(int id, String agentid, String zone, String picture, String title, String firstname, String lastname, String othername, String gender, String dateofbirth, String contact, String address, String dateofemployment, int active) {
        this.id = id;
        this.agentid = agentid;
        this.zone = zone;
        this.picture = picture;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.othername = othername;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.contact = contact;
        this.address = address;
        this.dateofemployment = dateofemployment;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateofemployment() {
        return dateofemployment;
    }

    public void setDateofemployment(String dateofemployment) {
        this.dateofemployment = dateofemployment;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
