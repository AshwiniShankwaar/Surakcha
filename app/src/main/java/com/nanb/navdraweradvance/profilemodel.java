package com.nanb.navdraweradvance;

public class profilemodel {
    private String name,phonenumber,email,Permanentaddress,currentaddress;

    public profilemodel(String name, String phonenumber, String email, String permanentaddress, String currentaddress) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.email = email;
        this.Permanentaddress = permanentaddress;
        this.currentaddress = currentaddress;
    }

    public profilemodel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermanentaddress() {
        return Permanentaddress;
    }

    public void setPermanentaddress(String permanentaddress) {
        this.Permanentaddress = permanentaddress;
    }

    public String getCurrentaddress() {
        return currentaddress;
    }

    public void setCurrentaddress(String currentaddress) {
        this.currentaddress = currentaddress;
    }
}
