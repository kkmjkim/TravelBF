package com.example.finaltbf;

public class PlaceItem {
    private String title;
    private String addr;

    public PlaceItem(String title, String addr) {
        this.title = title;
        this.addr = addr;
    }

    public PlaceItem(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}