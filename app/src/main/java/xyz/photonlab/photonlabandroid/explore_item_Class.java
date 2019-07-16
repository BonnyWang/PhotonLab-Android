package xyz.photonlab.photonlabandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class explore_item_Class {

    private String link;
    private String imageLink;
    private String title;

    explore_item_Class(){

    }

    explore_item_Class(String imageLink, String link, String title){
        this.imageLink = imageLink;
        this.link = link;
        this.title = title;
    }

    public String getLink(){
        return link;
    }

    public String getImageLink(){
        return imageLink;
    }

    public String getTitle(){return title;}

}
