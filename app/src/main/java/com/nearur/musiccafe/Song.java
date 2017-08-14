package com.nearur.musiccafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by mrdis on 7/17/2017.
 */

public class Song {
    int id;
    byte[] icon;
    String path;
    String name,artist,album;
    public  Song(){
        artist="";
        album="";
    }

    public Song(int id,byte[] icon, String path, String name, String artist, String album) {
        this.id=id;
        this.icon = icon;
        this.path = path;
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
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
