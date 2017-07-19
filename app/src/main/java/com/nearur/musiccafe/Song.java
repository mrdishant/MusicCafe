package com.nearur.musiccafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by mrdis on 7/17/2017.
 */

public class Song {
    Bitmap icon;
    String path;
    String name,artist,album;
    public  Song(){
        icon=null;
        artist="";
        album="";
    }



    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }


}
