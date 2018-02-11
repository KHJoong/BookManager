package com.example.kimhj.mybookmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by kimhj on 2017-05-01.
 */

public class Book {
    byte[] poster;
    String title;
    String author;
    String publisher;
    String publish_date;

    boolean checkedItem;

    Book(byte[] po, String t, String a, String p, String pd){
        poster = po;
        title = t;
        author = a;
        publisher = p;
        publish_date = pd;
    }

    public byte[] getPoster(){
        return poster;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getPublisher(){
        return publisher;
    }

    public String getPublish_date(){
        return publish_date;
    }

    public boolean getCheckedItem(){
        return checkedItem;
    }

    public void setCheckedItem(boolean b){
        checkedItem = b;
    }

}
