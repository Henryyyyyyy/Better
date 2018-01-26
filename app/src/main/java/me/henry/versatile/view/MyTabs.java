package me.henry.versatile.view;

/**
 * Created by henry on 2018/1/2.
 */

public class MyTabs {
    public String type;
    public int selectedRes;
    public int un_selectedRes;

    public MyTabs(String type, int selectedRes, int un_selectedRes) {
        this.type = type;
        this.selectedRes = selectedRes;
        this.un_selectedRes = un_selectedRes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSelectedRes() {
        return selectedRes;
    }

    public void setSelectedRes(int selectedRes) {
        this.selectedRes = selectedRes;
    }

    public int getUn_selectedRes() {
        return un_selectedRes;
    }

    public void setUn_selectedRes(int un_selectedRes) {
        this.un_selectedRes = un_selectedRes;
    }
}
