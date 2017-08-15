package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Abhinav on 04-Apr-17.
 */
public class PlaylistOption extends Dialog implements AdapterView.OnItemClickListener  {

    ListView listView;
    PlaylistTab playlistTab;
    String playlist_id;
    MainActivity mainActivity;
    Songs songs;
    OnClickInFragment onClickInFragment;
    public PlaylistOption(Fragment fragment,String playlist_id) {
        super(fragment.getContext());
        playlistTab=(PlaylistTab)fragment;
        this.playlist_id=playlist_id;
        mainActivity=(MainActivity) playlistTab.getActivity();
        onClickInFragment=mainActivity;
        songs=mainActivity.saagp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_option);
        listView=(ListView)findViewById(R.id.listView8);
        ArrayList<String> list=new ArrayList<>();
        list.add("Play");
        if(!playlist_id.equals("Recently Added")) {
            list.add("Rename");
            list.add("Delete");
        }
        listView.setAdapter(new ArrayAdapter<String>(playlistTab.getContext(),R.layout.list_view,R.id.textview3,list));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position==0)
        {
            Cursor c;
            if(playlist_id.equals("Recently Added"))
                c=playlistTab.getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DATE_ADDED);
           else
            c=playlistTab.getActivity().getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.valueOf(playlist_id)),null,null,null,MediaStore.Audio.Playlists.Members.PLAY_ORDER);

            if(c.getCount()>0)
            onClickInFragment.initiateSong(c,0);
            dismiss();
        }
        else if(position==1)
        {

            RenameDialog renameDialog=new RenameDialog(playlistTab,playlist_id);
            renameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            renameDialog.show();
            dismiss();
        }
        else if(position==2)
        {
            if(!playlist_id.equals("Recently Added")) {
                mainActivity.getContentResolver().delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, MediaStore.Audio.Playlists._ID + "=?", new String[]{playlist_id});
                playlistTab.playlist = songs.getName();
                playlistTab.playlist.add("Recently Added");
                playlistTab.arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_view, R.id.textview3, playlistTab.playlist);
                playlistTab.listView.setAdapter(playlistTab.arrayAdapter);
            }
            dismiss();
        }
    }
}
