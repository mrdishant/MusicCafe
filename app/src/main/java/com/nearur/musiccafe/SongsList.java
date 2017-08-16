
package com.nearur.musiccafe;


import android.app.ProgressDialog;
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
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongsList extends Fragment implements AdapterView.OnItemClickListener,U {

    int i=1;
    ListView listView;
    MyAdapter adapter;
    Bitmap songImage;
    ContentResolver resolver;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
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
        resolver = getContext().getContentResolver();
        songImage = BitmapFactory.decodeResource(getResources(), R.drawable.music);
        preferences = getContext().getSharedPreferences("music", Context.MODE_PRIVATE);
        editor = preferences.edit();
       if (preferences.contains("sync")) {
           new task().execute();
        } else {
           new task1().execute();
           editor.putString("sync","hai");
           editor.commit();
        }
        return v;
    }
    ArrayList<Song> findsong(File path){
        ArrayList<Song>a1=new ArrayList<>();
        File file = path;
        File[] filenames = file.listFiles();
        for (File x : filenames) {

            if(x.isDirectory() && !x.isHidden()){
                a1.addAll(findsong(x));
            }
            if (x.getName().endsWith(".mp3")) {
                Song s = new Song();
                s.id=i++;
                mediaMetadataRetriever.setDataSource(x.getAbsolutePath());
                art = mediaMetadataRetriever.getEmbeddedPicture();
                if (art != null) {
                    s.setIcon(art);
                }
                s.path = x.getAbsolutePath();
                s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                s.setArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                s.setName(x.getName());
                a1.add(s);
                insert(s);
            }
        }
        return a1;
    }

    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                 U c=(U)getContext();
                 c.set(a.get(i),true);
    }
    public void ad(){
        Comparator<Song> comparator=new Comparator<Song>() {
            @Override
            public int compare(Song o, Song t1) {
                return o.name.compareTo(t1.name);
            }
        };
        Collections.sort(a,comparator);
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
        values.put(Util.id,song.id);
        values.put(Util.sname,song.name);
        values.put(Util.artist,song.artist);
        values.put(Util.album,song.album);
        values.put(Util.path,song.path);
        values.put(Util.icon,song.icon);
        resolver.insert(Util.u,values);
    }


    class task  extends AsyncTask<Void,Void,Void>{


        int i=1;
        @Override
        protected Void doInBackground(Void... voids) {
            String[] p={"Name","Artist","Album","Path","Image","Id"};
            Cursor c=resolver.query(Util.u,p,null,null,null);
            if(c!=null) {
                while (c.moveToNext()) {
                    a.add(new Song(c.getInt(5), c.getBlob(4), c.getString(3), c.getString(0), c.getString(1), c.getString(2)));
                }
            }
            c.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ad();
        }
    }
    class task1 extends AsyncTask<Void,Integer,ArrayList<Song>>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog=ProgressDialog.show(getContext(),"Please wait","Scanning...",false);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {

            a=findsong(Environment.getExternalStorageDirectory());
            publishProgress(1);
            return a;
        }

        @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            super.onPostExecute(songs);
            progressDialog.dismiss();
            ad();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),"Scanned",Toast.LENGTH_LONG).show();
        }
    }

}
