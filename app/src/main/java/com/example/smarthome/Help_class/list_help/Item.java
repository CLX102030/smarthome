package com.example.smarthome.Help_class.list_help;

public class Item {
    private int iId;
    private String iName;

    public Item() {
    }

    public Item( String iName) {

        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
