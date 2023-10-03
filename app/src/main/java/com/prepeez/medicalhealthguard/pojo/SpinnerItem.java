package com.prepeez.medicalhealthguard.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class SpinnerItem {
    private String title;
    private boolean selected;

    public SpinnerItem() {

    }
    public SpinnerItem(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
