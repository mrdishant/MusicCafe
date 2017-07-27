
package com.nearur.musiccafe;


import android.app.VoiceInteractor;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.service.voice.VoiceInteractionService;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongsList extends Fragment implements AdapterView.OnItemClickListener,U {
    String path;
    ListView listView;
    MyAdapter adapter;
    Bitmap songImage;
    ContentResolver resolver;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    MediaPlayer mp;
    StringBuffer buffer=new StringBuffer();

    ArrayList<Song> a;
    MediaMetadataRetriever mediaMetadataRetriever;
    byte[] art;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs_list, container, false);
        listView = v.findViewById(R.id.listview);
        a = new ArrayList<>();
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mp = new MediaPlayer();
        resolver = getContext().getContentResolver();
        songImage = BitmapFactory.decodeResource(getResources(), R.drawable.music);
        preferences = getContext().getSharedPreferences("music", Context.MODE_PRIVATE);
        editor = preferences.edit();
       if (preferences.contains("sync")) {
            get();
        } else {


            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path);
            String[] filenames = file.list();
            for (String x : filenames) {
                String path2 = file.getAbsolutePath() + "/" + x;
                File file2 = new File(path2);
                String[] filenames2 = file2.list();
                if (filenames2 != null) {
                    for (String h : filenames2) {
                        if (h.endsWith(".mp3")) {
                            Song s = new Song();
                            mediaMetadataRetriever.setDataSource(path2 + "/" + h);
                            art = mediaMetadataRetriever.getEmbeddedPicture();
                            if (art != null) {
                                songImage = BitmapFactory
                                        .decodeByteArray(art, 0, art.length);
                                s.setIcon(songImage);
                            }
                            s.path = path2 + "/" + h;
                            s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                            s.setArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                            s.setName(h);
                            a.add(s);
                            insert(s);

                        }
                    }
                }
                if (x.endsWith(".mp3")) {
                    Song s = new Song();
                    mediaMetadataRetriever.setDataSource(path + "/" + x);
                    art = mediaMetadataRetriever.getEmbeddedPicture();
                    if (art != null) {
                        songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length);
                        s.setIcon(songImage);
                    }
                    s.path = path + "/" + x;
                    s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                    s.setArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                    s.setName(x);
                    a.add(s);
                    insert(s);
                }

            }
       /* if(filenames!=null&&filenames.length>0){
            for(File f:filenames){
                if(f.isDirectory()){
                    scanD(f);
                }
                else if(f.getName().endsWith(".mp3")){
                    Song s=new Song();
                    mediaMetadataRetriever.setDataSource(f.getAbsolutePath());
                    art=mediaMetadataRetriever.getEmbeddedPicture();
                    if(art!=null) {
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length);
                        s.setIcon(songImage);
                    }
                    s.path=f.getAbsolutePath();
                    s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                    s.setArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                    s.setName(f.getName());
                    a.add(s);
                }
            }
        }*/
              editor.putString("sync","hai");
              editor.commit();

            ad();


        }
        return v;
    }

    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {

                 U c=(U)getContext();
                 c.set(a.get(i),true);
    }
    public void ad(){
        adapter = new MyAdapter(getContext(), R.layout.song, a);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        if (preferences.contains("path")) {
            for (Song s : a) {
                if (s.path.equals(preferences.getString("path", ""))) {
                    U c = (U) getContext();
                    c.set(s, false);
                    break;
                }
            }

        }
    }
    public void set(Song m,boolean s){

    }

    void insert(Song song){
        ContentValues values=new ContentValues();

        values.put(Util.sname,song.name);
        values.put(Util.artist,song.artist);
        values.put(Util.album,song.album);
        values.put(Util.path,song.path);

        if(song.icon!=null) {
            Bitmap bmp = song.getIcon();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            values.put(Util.icon, String.valueOf(byteArray));
        }
        else{
            values.put(Util.icon,String.valueOf(song.icon));
        }
        Uri x=resolver.insert(Util.u,values);

        Toast.makeText(getContext(),"Songs Added: "+x.getLastPathSegment(),Toast.LENGTH_SHORT).show();

    }

    void get(){

        String[] p={"Name","Artist","Album","Path","Image"};
        Cursor c=resolver.query(Util.u,p,null,null,null);
        Bitmap image=null;
        if(c!=null) {
            while (c.moveToNext()) {

                mediaMetadataRetriever.setDataSource(c.getString(3));
                art=mediaMetadataRetriever.getEmbeddedPicture();
                if (art != null) {
                    image = BitmapFactory
                            .decodeByteArray(art, 0, art.length);
                }
                a.add(new Song(image,c.getString(3),c.getString(0),c.getString(1),c.getString(2)));
                image=null;
            }
            ad();

        }


    }


    }
