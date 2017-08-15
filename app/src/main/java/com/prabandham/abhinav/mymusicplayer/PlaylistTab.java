package com.prabandham.abhinav.mymusicplayer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Abhinav on 28-Dec-16.
 */
public class PlaylistTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listView;
    MainActivity mainActivity;
    Playlists playlists;
    ArrayList<String> playlist=new ArrayList<>();
    private FragmentTransaction ft;
    ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlist_tab, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            listView = (ListView) view.findViewById(R.id.listView6);
            mainActivity = (MainActivity) getActivity();
            playlists = mainActivity.saagp;
            playlist = playlists.getName();
            playlist.add("Recently Added");
            //playlist.add("Last Played");
            arrayAdapter = new ArrayAdapter(getContext(), R.layout.list_view, R.id.textview3, playlist);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
        }catch (Exception e)
        {

        }
        //Toast.makeText(getContext(), "Playlisttab onviewcreated", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaylistSong playlistSong;
        if(position<playlist.size()-1)
        {
            playlistSong=PlaylistSong.getInstance(playlists.getId(playlist.get(position)),playlist.get(position));

        }
        else{
            playlistSong=PlaylistSong.getInstance(playlist.get(position),playlist.get(position));

        }
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.mainrl, playlistSong).addToBackStack(null).commit();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PlaylistOption playlistOption;
        if(position<playlist.size()-1) {
           playlistOption = new PlaylistOption(this, playlists.getId(playlist.get(position)));

        }
        else{
            playlistOption=new PlaylistOption(this,(playlist.get(position)));
        }
        playlistOption.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        playlistOption.show();
        return true;
    }
}
