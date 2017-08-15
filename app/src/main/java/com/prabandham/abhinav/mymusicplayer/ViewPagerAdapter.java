package com.prabandham.abhinav.mymusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Abhinav on 17-Jun-17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Cursor cursor;
    private int album_key;
    private String album_image;
    private int album;
    private int artist;
    Context context;
    int count=0;
    public ViewPagerAdapter(FragmentManager fm,Cursor cursor,Context context)
    {
        super(fm);
        this.cursor=new MergeCursor(new Cursor[]{cursor});
        initializeColumns();
        this.context=context;
        Log.v("adapter","constructor");

    }

    private void initializeColumns() {
        album_key=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);
        artist=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        album=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        count=cursor.getCount();
    }

    public void swapCursor(Cursor cursor)
    {
        this.cursor=cursor;
        initializeColumns();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position)
    {
       if(cursor.moveToPosition(position))
       {
           album_image=SAAGP.getInstance(context).getAlbumArt(cursor.getString(album_key));
           return ViewPagerFragment.getInstance(album_image, cursor.getString(artist)+"",cursor.getString(album)+"");
       }
       else
           return ViewPagerFragment.getInstance("","","");
    }

    public void addCursor(Cursor cursor)
    {
        swapCursor(new MergeCursor(new Cursor[]{this.cursor,cursor}));
    }
    @Override
    public int getCount()
    {
        if(cursor == null) {
            return 0;
        } else if(!cursor.isClosed())
            return cursor.getCount();
        else
            return count;
    }

    public String getTitle(int position)
    {

        if(cursor.moveToPosition(position))
        {
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            return title+"";
        }
        return "";
    }
    public String getPath(int position)
    {
        if(cursor.moveToPosition(position))
        {

            String path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))+"";
            return path;
        }
        return "";
    }

    public String getDuration(int position)
    {
        if(cursor.moveToPosition(position)) {
            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) + "";
            if (duration == null || "".equals(duration)) {
                return "0";
            } else {
                return duration;
            }
        }
        return "0";
    }

    public String getArtist(int position)
    {
        if(cursor.moveToPosition(position))
        {
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            return artist+"";
        }
        return "";
    }

    public String getAlbum(int position)
    {
        if(cursor.moveToPosition(position))
        {
            String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            return album+"";
        }
        return "";
    }
    public String getAlbumKey(int position)
    {
        if(cursor.moveToPosition(position))
        {
            String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY));
            return album+"";
        }
        return "";
    }
    public String getArtistId(int position)
    {
        if(cursor.moveToPosition(position))
        {
            String artist_id=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            return artist_id+"";
        }
        return "";

    }
    public String getAlbumArt(int position)
    {
        if(cursor.moveToPosition(position)) {
            return SAAGP.getInstance(context).getAlbumArt(cursor.getString(album_key));
        }
        return "";
    }

    public Cursor getCursorAtPosition(int position)
    {
        MatrixCursor matrixcursor=new MatrixCursor(cursor.getColumnNames());
        ArrayList<Object>  x=new ArrayList<>();;
        if(cursor.moveToPosition(position)) {
            for (int i = 0; i < matrixcursor.getColumnCount(); i++) {

                x.add(cursor.getString(cursor.getColumnIndex(matrixcursor.getColumnName(i))));
            }
        }
        matrixcursor.addRow(x.toArray());
        x=null;
        return matrixcursor;
    }

}
