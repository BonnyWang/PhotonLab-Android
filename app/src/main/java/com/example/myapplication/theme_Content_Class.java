package com.example.myapplication;

public class theme_Content_Class {

    String subtitle;
    String content;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public theme_Content_Class(String subtitle, String content) {
        this.subtitle = subtitle;
        this.content = content;
    }
}
