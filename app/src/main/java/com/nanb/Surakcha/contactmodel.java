package com.nanb.Surakcha;

import java.io.Serializable;

public class contactmodel implements Serializable {
    private String name;
    private String phoneNumber;
    private String id;

    public contactmodel(String name, String phoneNumber,String id) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;

    }

    public contactmodel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
