package com.prepeez.medicalhealthguard.pojo;

import java.util.HashMap;

/**
 * Created by Nana on 12/16/2017.
 */

public class SupportDocument {
    HashMap<String, Object> timestampCreated;
    private String title;
    private String metaData;
    private String url;
    private String ext;

    public SupportDocument()
    {

    }

    public SupportDocument(String title, String metaData, String url, String ext) {
        HashMap<String, Object> timestampNow = new HashMap<>();
        //timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.title = title;
        this.metaData = metaData;
        this.url = url;
        this.ext = ext;
    }

    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }
    //@Exclude
    public long getTimestampCreatedLong(){
        if (timestampCreated == null) {
            return 0L;
        }
        return (long)timestampCreated.get("timestamp");
    }

    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
