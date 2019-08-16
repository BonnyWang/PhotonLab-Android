package xyz.photonlab.photonlabandroid.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class explore_item_Class {

    public final static int CREATIVE = 0b01;
    public final static int TUTORIAL = 0b10;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @IntDef({CREATIVE, TUTORIAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {

    }

    private String link;

    private String imageLink;
    private String title;
    private int category = 0x00;
    private String description;

    public void setLink(String link) {
        this.link = link;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void addCategory(@Category int category) {
        this.category = this.category | category;
    }

    public boolean isCreative() {
        return (category & CREATIVE) == CREATIVE;
    }

    public boolean isTutorial() {
        return (category & TUTORIAL) == TUTORIAL;
    }
}
