package com.nanb.navdraweradvance;

public class settingmodel {
    private int msgid;
    private int locationservice;

    public settingmodel(int msgid, int locationservice) {
        this.msgid = msgid;
        this.locationservice = locationservice;
    }

    public settingmodel() {
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public int getLocationservice() {
        return locationservice;
    }

    public void setLocationservice(int locationservice) {
        this.locationservice = locationservice;
    }
}
