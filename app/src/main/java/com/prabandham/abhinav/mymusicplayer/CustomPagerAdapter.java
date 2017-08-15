package com.prabandham.abhinav.mymusicplayer;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
/**
 * Created by Abhinav on 8/30/2016.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    //SongList sl;
    //Album album;
    //Bundle  bundle;

    public CustomPagerAdapter(FragmentManager fs,Context context)
    {
        super(fs);
        this.context=context;

      /* sl=((MainActivity)context).sl;
        album=((MainActivity)context).album;
        bundle=new Bundle();
        bundle.putSerializable("SongList",sl);
        bundle.putSerializable("Album",album);*/

    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {

            Fragment fragment = new SongTab();

            return fragment;
        }
        else if(position==1)
        {
            Fragment fragment=new AlbumTab();
           // songtab.setArguments(bundle);
            return fragment;
        }
        else if(position==2)
        {
            Fragment fragment=new ArtistTab();
            return fragment;
        }
        else if(position==3)
        {
            Fragment fragment=new GenreTab();
            return fragment;
        }
        else{
            Fragment fragment=new PlaylistTab();
            return fragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
       if(position==0)
       {
           return "SONGS";
       }
        else if(position==1)
       {
           return "ALBUMS";
       }
        else if(position==2){
           return "ARTISTS";
       }
        else if(position==3){
           return"GENRES";
       }
        else{
           return "PLAYLISTS";
       }
    }


    @Override
    public int getCount() {
        return 5;
    }
}
