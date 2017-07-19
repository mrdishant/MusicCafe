
package com.nearur.musiccafe;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
    SharedPreferences preferences;

    MediaPlayer mp;
    StringBuffer buffer=new StringBuffer();

    ArrayList<Song> a;
    MediaMetadataRetriever mediaMetadataRetriever;
    byte[] art;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_songs_list, container, false);
        listView =v.findViewById(R.id.listview);
        a=new ArrayList<>();
        mp=new MediaPlayer();
        preferences=getContext().getSharedPreferences("music", Context.MODE_PRIVATE);

        mediaMetadataRetriever=new MediaMetadataRetriever();

        path= Environment.getExternalStorageDirectory().getAbsolutePath();
        File file=new File(path);
        String[] filenames=file.list();
        for(String x:filenames){
            String path2=file.getAbsolutePath()+"/"+x;
            File file2=new File(path2);
            String[] filenames2=file2.list();
            if(filenames2!=null) {
                for (String h : filenames2) {
                    if (h.endsWith(".mp3")) {
                        Song s = new Song();
                        mediaMetadataRetriever.setDataSource(path2 + "/" + h);
                        art = mediaMetadataRetriever.getEmbeddedPicture();
                        if (art != null ) {
                           songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length);
                            s.setIcon(songImage);
                        }
                        s.path = path2 + "/" + h;
                        s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                        s.setArtist( mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                        s.setName(h);
                        a.add(s);

                    }
                }
            }
            if(x.endsWith(".mp3")){
                Song s=new Song();
               mediaMetadataRetriever.setDataSource(path+"/"+x);
                art=mediaMetadataRetriever.getEmbeddedPicture();
                if (art != null) {
                    songImage = BitmapFactory
                            .decodeByteArray(art, 0, art.length);
                    s.setIcon(songImage);
                }
                s.path=path+"/"+x;
                s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                s.setArtist( mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                s.setName(x);
                a.add(s);
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

        adapter=new MyAdapter(getContext(),R.layout.song,a);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        System.out.println(buffer);

        if(preferences.contains("path")){
            for(Song s:a){
                if(s.path.equals(preferences.getString("path",""))){
                    U c=(U)getContext();
                    c.set(s,false);
                    break;
                }
            }
        }
        return v;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 U c=(U)getContext();
                 c.set(a.get(i),true);
    }
    public void set(Song m,boolean s){

    }


    public class task extends AsyncTask<ArrayList<Song>,Void,ArrayList<Song>> {
        ArrayList<Song> a = new ArrayList<>();
        @Override
        protected ArrayList<Song> doInBackground(ArrayList<Song>... arrayLists) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File f = new File(path);
            scan(f);
            return a;
        }

        public void scan(File d) {
            if (d != null) {
                File[] filenames = d.listFiles();
                if (filenames != null) {
                    for (File f : filenames) {
                        if (f.isDirectory()) {
                            scan(f);
                        } else if (f.getName().endsWith(".mp3")) {
                            Song s = new Song();
                            mediaMetadataRetriever.setDataSource(f.getAbsolutePath());
                            byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
                            if (art != null) {
                                Bitmap songImage = BitmapFactory
                                        .decodeByteArray(art, 0, art.length);
                                s.setIcon(songImage);
                            }
                            s.path = f.getAbsolutePath();
                            s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                            s.setArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                            s.setName(f.getName());
                            a.add(s);
                        }
                    }

                }

            }
        }

        @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            adapter=new MyAdapter(getContext(),R.layout.song,songs);
            listView.setAdapter(adapter);
            Toast.makeText(getContext(),"Scan Done",Toast.LENGTH_LONG).show();
            }
    }




    }
