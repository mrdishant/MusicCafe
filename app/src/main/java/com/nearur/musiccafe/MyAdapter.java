
package com.nearur.musiccafe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends ArrayAdapter {

    Context context;int resource; ArrayList<Song> objects;
    Bitmap image;

    public MyAdapter(Context context,int resource, ArrayList<Song> objects) {
        super(context, resource, objects);

        this.context=context;
        this.resource=resource;
        this.objects=objects;
        image= BitmapFactory.decodeResource(context.getResources(),R.drawable.music);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=LayoutInflater.from(context).inflate(resource,parent,false);


        ImageView img=v.findViewById(R.id.imageView);
        TextView txtname=v.findViewById(R.id.checkedTextView);
        TextView txtalbum=v.findViewById(R.id.textView2);
        TextView txtartist=v.findViewById(R.id.textView3);


        Song song=objects.get(position);
        if(song.getIcon()==null){
            img.setImageBitmap(image);
        }
        else {
            img.setImageBitmap(song.getIcon());
        }

        txtname.setText(song.getName().substring(0,song.getName().indexOf(".mp3")));
        txtalbum.setText(song.getAlbum());
        txtartist.setText(song.getArtist());



        return v;
    }
}
