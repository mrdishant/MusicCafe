
package com.nearur.musiccafe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Splash extends AppCompatActivity {
    ImageView img;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        img=(ImageView)findViewById(R.id.imageView2);
        txt=(TextView)findViewById(R.id.textView5);
        h.sendEmptyMessageDelayed(102,0);
        h.sendEmptyMessageDelayed(101,2000);
    }
   Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==102){
                Animation animation= AnimationUtils.loadAnimation(Splash.this,R.anim.splash1);
                img.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);
                img.startAnimation(animation);
                txt.startAnimation(animation);
            }

            if(msg.what==101){
                Intent i=new Intent(Splash.this,MainActivity.class);
                overridePendingTransition(R.anim.splash,R.anim.splash2);
                startActivity(i);
                finish();
            }
        }
    };

}
