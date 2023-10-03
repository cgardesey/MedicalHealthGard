package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class Customer {
    private String userId;
    private boolean userRegistered;
    //HashMap<String, Object> timestampCreated;
    private String profilePicUrl;
    private String name;
    private String contact;

    public Customer(){

    }

    public Customer(String userId, boolean userRegistered, String profilePicUrl, String name, String contact) {
        this.userId = userId;
//        HashMap<String, Object> timestampNow = new HashMap<>();
//        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
//        this.timestampCreated = timestampNow;
        this.userRegistered = userRegistered;
        this.profilePicUrl = profilePicUrl;
        this.name = name;
        this.contact = contact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isUserRegistered() {
        return userRegistered;
    }

    public void setUserRegistered(boolean userRegistered) {
        this.userRegistered = userRegistered;
    }

//    public HashMap<String, Object> getTimestampCreated(){
//        return timestampCreated;
//    }
//    @Exclude
//    public long getTimestampCreatedLong(){
//        return (long)timestampCreated.get("timestamp");
//    }
//
//    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
//        this.timestampCreated = timestampCreated;
//    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
