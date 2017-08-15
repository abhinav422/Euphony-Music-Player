package com.prabandham.abhinav.mymusicplayer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Abhinav on 9/1/2016.
 */
public class ImageAdapter extends BaseAdapter{
     Context context;
    private ArrayList<String> album_art;
    private ArrayList<String> albums;
    Cursor cursor;
    private CustomTransformation customTransformation;

    public ImageAdapter(Context context, Cursor cursor) {
        this.context=context;
        this.cursor=cursor;
        customTransformation=new CustomTransformation(150,150);
    }

    static class ViewHolder
    {
        ImageView imageView;
        TextView textView;
        int position;
    }

    @Override
    public int getCount() {
       return cursor.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater)((Activity)context).getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.album_image,null);
            holder=new ViewHolder();
            holder.imageView=(ImageView) convertView.findViewById(R.id.imageView);
            holder.textView=(TextView)convertView.findViewById(R.id.textView17);

        }
        else {
           holder=(ViewHolder)convertView.getTag();
            if(holder.position==position)
            {
                return convertView;
            }

        }

        cursor.moveToPosition(position);
        Picasso.with(context).load(new File(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))+"")).transform(customTransformation).placeholder(R.drawable.musicicon1).fit().centerInside().into(holder.imageView);
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))+"");
        holder.position=position;
        convertView.setTag(holder);
        return convertView;


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public String getItem(int position) {
        if(cursor!=null)
        {
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
        }
        return null;
    }

}


