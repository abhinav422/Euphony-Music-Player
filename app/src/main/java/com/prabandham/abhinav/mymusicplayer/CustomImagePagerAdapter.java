package com.prabandham.abhinav.mymusicplayer;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Abhinav on 21-Mar-17.
 */
public class CustomImagePagerAdapter extends FragmentStatePagerAdapter {

    private Cursor cursor;
    int flag;
    Songs songs;
    ArrayList<String> x;
    public CustomImagePagerAdapter(FragmentManager fm, Cursor external,int flag) {
        super(fm);
        this.cursor=external;
        this.flag=flag;
    }

    public CustomImagePagerAdapter(FragmentManager fm, Cursor external, int i, Songs songs) {
        super(fm);
        this.cursor=external;
        this.flag=i;
        this.songs=songs;
    }

    public CustomImagePagerAdapter(FragmentManager fm, Cursor c, int i, ArrayList<String> x) {
        super(fm);
        this.x=x;
        this.cursor=c;
        this.flag=i;

    }

    @Override
    public Fragment getItem(int position) {
        cursor.moveToPosition(position);
        if(flag==0)
       return  PageFragment.create(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART)),cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM)));
        else
            return PageFragment.create(x.get(position),cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM)));
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    public Cursor getCursor()
    {
        return cursor;
    }

    public String getAlbumKey(int position)
    {
        cursor.moveToPosition(position);
        return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_KEY));
    }
}
