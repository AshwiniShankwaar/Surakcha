package com.nanb.Surakcha;

public class msgmodel {

    private int id;
    private String title;
    private String msg;

    public msgmodel(int id, String title, String msg) {
        this.id = id;
        this.title = title;
        this.msg = msg;
    }

    public msgmodel() {
    }

    @Override
    public String toString() {
        return "msgmodel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
