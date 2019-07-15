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

    explore_item_Class(String imageLink, String link){
        this.imageLink = imageLink;
        this.link = link;
    }

    public String getLink(){
        return link;
    }

    public String getImageLink(){
        return imageLink;
    }

}
