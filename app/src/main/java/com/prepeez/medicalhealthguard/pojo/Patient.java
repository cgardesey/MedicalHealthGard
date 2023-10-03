package com.prepeez.medicalhealthguard.pojo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class Patient implements Serializable {
    @Expose
    private int id;
    @Expose
    private String patientid;
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
    private String maritalstatus;
    @Expose
    private String contact;
    @Expose
    private String address;
    @Expose
    private String location;
    @Expose
    private String occupation;
    @Expose
    private String nextofkintitle;
    @Expose
    private String nextofkinfirstname;
    @Expose
    private String nextofkinlastname;
    @Expose
    private String nextofkinothername;
    @Expose
    private String nextofkingender;
    @Expose
    private String nextofkindateofbirth;
    @Expose
    private String nextofkinmaritalstatus;
    @Expose
    private String nextofkincontact;
    @Expose
    private String nextofkinaddress;
    @Expose
    private String nextofkinlocation;
    @Expose
    private String nhisnumber;
    @Expose
    private String nhispicture;
    @Expose
    private String abobloodgroup;
    @Expose
    private String sicklecellbloodgroup;
    @Expose
    private String allergichistory;
    @Expose
    private String drughistory;
    @Expose
    private String pastsurgicalhistory;
    @Expose
    private String pasthistory;
    @Expose
    private String familyhistory;
    @Expose
    private String ageofmenarche;
    @Expose
    private String gravidity;
    @Expose
    private String accounttype;
    @Expose
    private String grouptype;
    @Expose
    private String groupid;
    @Expose
    private int active;
    @Expose
    private String sms;
    @Expose
    private String smsstatus;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;

    public Patient(){

    }

    public Patient(int id, String patientid, String zone,String picture, String title, String firstname, String lastname, String othername, String gender, String dateofbirth, String maritalstatus, String contact, String address, String location, String occupation, String nextofkintitle, String nextofkinfirstname, String nextofkinlastname, String nextofkinothername, String nextofkingender, String nextofkindateofbirth, String nextofkinmaritalstatus, String nextofkincontact, String nextofkinaddress, String nextofkinlocation, String nhisnumber, String nhispicture, String abobloodgroup, String sicklecellbloodgroup, String allergichistory, String drughistory, String pastsurgicalhistory, String pasthistory, String familyhistory, String ageofmenarche, String gravidity, String accounttype, String grouptype, String groupid, int active, String sms, String smsstatus) {
        this.id = id;
        this.patientid = patientid;
        this.zone = zone;
        this.picture = picture;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.othername = othername;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.maritalstatus = maritalstatus;
        this.contact = contact;
        this.address = address;
        this.location = location;
        this.occupation = occupation;
        this.nextofkintitle = nextofkintitle;
        this.nextofkinfirstname = nextofkinfirstname;
        this.nextofkinlastname = nextofkinlastname;
        this.nextofkinothername = nextofkinothername;
        this.nextofkingender = nextofkingender;
        this.nextofkindateofbirth = nextofkindateofbirth;
        this.nextofkinmaritalstatus = nextofkinmaritalstatus;
        this.nextofkincontact = nextofkincontact;
        this.nextofkinaddress = nextofkinaddress;
        this.nextofkinlocation = nextofkinlocation;
        this.nhisnumber = nhisnumber;
        this.nhispicture = nhispicture;
        this.abobloodgroup = abobloodgroup;
        this.sicklecellbloodgroup = sicklecellbloodgroup;
        this.allergichistory = allergichistory;
        this.drughistory = drughistory;
        this.pastsurgicalhistory = pastsurgicalhistory;
        this.pasthistory = pasthistory;
        this.familyhistory = familyhistory;
        this.ageofmenarche = ageofmenarche;
        this.gravidity = gravidity;
        this.accounttype = accounttype;
        this.grouptype = grouptype;
        this.groupid = groupid;
        this.active = active;
        this.sms = sms;
        this.smsstatus = smsstatus;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
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

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNextofkintitle() {
        return nextofkintitle;
    }

    public void setNextofkintitle(String nextofkintitle) {
        this.nextofkintitle = nextofkintitle;
    }

    public String getNextofkinfirstname() {
        return nextofkinfirstname;
    }

    public void setNextofkinfirstname(String nextofkinfirstname) {
        this.nextofkinfirstname = nextofkinfirstname;
    }

    public String getNextofkinlastname() {
        return nextofkinlastname;
    }

    public void setNextofkinlastname(String nextofkinlastname) {
        this.nextofkinlastname = nextofkinlastname;
    }

    public String getNextofkinothername() {
        return nextofkinothername;
    }

    public void setNextofkinothername(String nextofkinothername) {
        this.nextofkinothername = nextofkinothername;
    }

    public String getNextofkingender() {
        return nextofkingender;
    }

    public void setNextofkingender(String nextofkingender) {
        this.nextofkingender = nextofkingender;
    }

    public String getNextofkindateofbirth() {
        return nextofkindateofbirth;
    }

    public void setNextofkindateofbirth(String nextofkindateofbirth) {
        this.nextofkindateofbirth = nextofkindateofbirth;
    }

    public String getNextofkinmaritalstatus() {
        return nextofkinmaritalstatus;
    }

    public void setNextofkinmaritalstatus(String nextofkinmaritalstatus) {
        this.nextofkinmaritalstatus = nextofkinmaritalstatus;
    }

    public String getNextofkincontact() {
        return nextofkincontact;
    }

    public void setNextofkincontact(String nextofkincontact) {
        this.nextofkincontact = nextofkincontact;
    }

    public String getNextofkinaddress() {
        return nextofkinaddress;
    }

    public void setNextofkinaddress(String nextofkinaddress) {
        this.nextofkinaddress = nextofkinaddress;
    }

    public String getNextofkinlocation() {
        return nextofkinlocation;
    }

    public void setNextofkinlocation(String nextofkinlocation) {
        this.nextofkinlocation = nextofkinlocation;
    }

    public String getNhisnumber() {
        return nhisnumber;
    }

    public void setNhisnumber(String nhisnumber) {
        this.nhisnumber = nhisnumber;
    }

    public String getNhispicture() {
        return nhispicture;
    }

    public void setNhispicture(String nhispicture) {
        this.nhispicture = nhispicture;
    }

    public String getAbobloodgroup() {
        return abobloodgroup;
    }

    public void setAbobloodgroup(String abobloodgroup) {
        this.abobloodgroup = abobloodgroup;
    }

    public String getSicklecellbloodgroup() {
        return sicklecellbloodgroup;
    }

    public void setSicklecellbloodgroup(String sicklecellbloodgroup) {
        this.sicklecellbloodgroup = sicklecellbloodgroup;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public String getAllergichistory() {
        return allergichistory;
    }

    public void setAllergichistory(String allergichistory) {
        this.allergichistory = allergichistory;
    }

    public String getDrughistory() {
        return drughistory;
    }

    public void setDrughistory(String drughistory) {
        this.drughistory = drughistory;
    }

    public String getPastsurgicalhistory() {
        return pastsurgicalhistory;
    }

    public void setPastsurgicalhistory(String pastsurgicalhistory) {
        this.pastsurgicalhistory = pastsurgicalhistory;
    }

    public String getPasthistory() {
        return pasthistory;
    }

    public void setPasthistory(String pasthistory) {
        this.pasthistory = pasthistory;
    }

    public String getFamilyhistory() {
        return familyhistory;
    }

    public void setFamilyhistory(String familyhistory) {
        this.familyhistory = familyhistory;
    }

    public String getAgeofmenarche() {
        return ageofmenarche;
    }

    public void setAgeofmenarche(String ageofmenarche) {
        this.ageofmenarche = ageofmenarche;
    }

    public String getGravidity() {
        return gravidity;
    }

    public void setGravidity(String gravidity) {
        this.gravidity = gravidity;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getSmsstatus() {
        return smsstatus;
    }

    public void setSmsstatus(String smsstatus) {
        this.smsstatus = smsstatus;
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
