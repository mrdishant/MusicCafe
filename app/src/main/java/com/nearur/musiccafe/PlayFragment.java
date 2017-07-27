
package com.nearur.musiccafe;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */


interface U{
    public void set(Song s,boolean a);
}
public class PlayFragment extends Fragment implements U,View.OnClickListener{

    ListView listView;
    MyAdapter adapter;
    ArrayList<Song> a;
    boolean play;
    ImageView btn;
    Context c;
    Song m;
    MediaPlayer mp;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_play, container, false);
        c=getContext();
        btn=(ImageView)v.findViewById(R.id.btnplay);
        btn.setVisibility(Button.INVISIBLE);
        btn.setOnClickListener(this);
        mp=new MediaPlayer();
        listView=(ListView)v.findViewById(R.id.listview2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),Current.class);
                intent.putExtra("progress",mp.getDuration());
                startActivity(intent);
            }
        });
        preferences=c.getSharedPreferences("music",Context.MODE_PRIVATE);
        editor=preferences.edit();
        return v;
    }

    public void set(Song song,boolean p){
        mp.reset();
        m=song;
        editor.putString("path",m.path);
        editor.putString("name",m.name);

        editor.commit();
        a=new ArrayList<>();
        song.setArtist("");
        song.setAlbum("");
        a.add(song);
        try {
            mp.setDataSource(m.path);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter=new MyAdapter(getContext(),R.layout.song,a);
        listView.setAdapter(adapter);
        btn.setVisibility(Button.VISIBLE);
        if(p) {
            btn.setBackgroundResource(R.drawable.pause);
            play = true;
            mp.start();
        }
        else{
            btn.setBackgroundResource(R.drawable.play);
        }
    }


    public void onClick(View v) {
        if (play) {
            btn.setBackgroundResource(R.drawable.play);
            play = false;
            mp.pause();
        } else {
            btn.setBackgroundResource(R.drawable.pause);
            play = true;
            mp.start();
        }
    }

}
