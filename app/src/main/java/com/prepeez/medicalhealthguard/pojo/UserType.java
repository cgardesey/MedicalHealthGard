package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class UserType {
    private  boolean signedin;
    private String agentid;

    public UserType()
    {

    }

    public UserType(boolean signedin, String agentid) {
        this.signedin = signedin;
        this.agentid = agentid;
    }

    public boolean isSignedin() {
        return signedin;
    }

    public void setSignedin(boolean signedin) {
        this.signedin = signedin;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }
}
