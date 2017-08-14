
package com.nearur.musiccafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Current extends AppCompatActivity {
    Song s;
    CheckedTextView checkedTextView;
    TextView textView,textView2;
    ImageView imageView;
    SeekBar seekBar;
    SharedPreferences preferences;
    MediaMetadataRetriever mediaMetadataRetriever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        Intent rcv=getIntent();
        checkedTextView=(CheckedTextView)findViewById(R.id.checkedTextViewc);
        imageView=(ImageView)findViewById(R.id.imageViewc);
        textView=(TextView)findViewById(R.id.textViewc);
        textView2=(TextView)findViewById(R.id.textViewc2);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        preferences=getSharedPreferences("music",MODE_PRIVATE);
        String path=preferences.getString("path","");
        s=new Song();
        mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        if (art != null ) {
            s.setIcon(art);
        }
        s.path = path;
        s.setAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        s.setArtist( mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        s.setName(preferences.getString("name",""));

        if(s.icon==null){
            imageView.setBackgroundResource(R.drawable.music);
        }
        else{
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(s.icon,0,s.icon.length));
        }

        checkedTextView.setText(s.name);
        textView.setText(s.getAlbum());
        textView2.setText(s.getArtist()+"\n"+s.path);
        int full=rcv.getIntExtra("full",0);
        int current=rcv.getIntExtra("progress",0);
        int perc=(current/full)*100;
        Toast.makeText(Current.this,String.valueOf(perc),Toast.LENGTH_LONG).show();
        seekBar.setProgress(perc);
    }
}
