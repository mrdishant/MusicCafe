package com.nearur.musiccafe;

import android.net.Uri;

/**
 * Created by mrdis on 7/20/2017.
 */

public class Util {

    public static  final int dversion=1;
    public static  final String db="SongsList";
    public static  final String tab="SongsNew1";

    public static final String id="Id";
    public static  final String sname="Name";
    public static  final String artist="Artist";
    public static  final String album="Album";
    public static  final String path="path";
    public static final String icon="Image";

    public static  final Uri u=Uri.parse("content://com.nearur.musiccafe/"+tab);

    public static  final String query="create table SongsNew1(" +
            "Id int primary key,"+
            "Name text," +
            "Artist text," +
            "Album text," +
            "path text ," +
            "Image BLOB"+
            ")";

}
