package com.nearur.musiccafe;

import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

public class Radio extends AppCompatActivity {
    String url="http://nearur.com/Qismat-(Mr-Jatt.com).mp3";
    ImageView img;
    TextView txtname,txtartist,txtalbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        img=(ImageView)findViewById(R.id.imageView3);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp=new MediaPlayer();
                try {
                    mp.setDataSource(Radio.this,Uri.parse(url));
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        txtname=(TextView)findViewById(R.id.textViewname);
        txtalbum=(TextView)findViewById(R.id.textViewalbum);
        txtartist=(TextView)findViewById(R.id.textViewartist);
        get();
    }

    void get() {
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(url,new HashMap<String, String>());
        byte[] a=mediaMetadataRetriever.getEmbeddedPicture();
        if(a!=null){
            img.setImageBitmap(BitmapFactory.decodeByteArray(a,0,a.length));
        }
        else{
            img.setBackgroundResource(R.drawable.music);
        }
        txtname.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        txtartist.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

        txtalbum.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));


    }
}
