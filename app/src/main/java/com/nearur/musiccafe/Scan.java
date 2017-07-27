
package com.nearur.musiccafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Scan extends AppCompatActivity {

    Button scan;
    TextView txt;
    MediaMetadataRetriever mediaMetadataRetriever;
    int count=1;
    StringBuffer buffer=new StringBuffer();
    ArrayList<Song> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        txt=(TextView)findViewById(R.id.textViewSong);
        scan=(Button)findViewById(R.id.buttonScan);
        mediaMetadataRetriever=new MediaMetadataRetriever();
        a=new ArrayList<>();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scan.getText().equals("Scan")){
                    scan.setText("Back");
                    new task().execute();
                }
                else{

                }
            }
        });

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
            if (d != null && d.length()>0) {
                File[] filenames = d.listFiles();
                if (filenames != null && filenames.length>0 ) {
                    for (File f:filenames) {
                        if (f.isDirectory()&& f.getName()!="Android") {
                           try{
                               scan(f);
                           }
                           catch (Exception e){
                               continue;
                           }
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
            else{
                return;
            }
        }

    }

}
