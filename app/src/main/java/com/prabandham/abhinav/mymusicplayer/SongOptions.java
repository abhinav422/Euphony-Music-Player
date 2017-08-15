package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Abhinav on 25-Jan-17.
 */
public class SongOptions extends Dialog implements AdapterView.OnItemClickListener {
    Fragment fragment;
    ListView listView;
    SelectedItem selectedItem;
    Songs songs;
    Cursor cursor;
    public SongOptions(Fragment fragment,Cursor cursor) {
        super(fragment.getContext());
        this.fragment=fragment;
        this.cursor=cursor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_options);
        listView=(ListView)findViewById(R.id.listView7);
        ArrayList<String> list=new ArrayList<>();
        list.add("Add to Playlist");
        list.add("Send");
        if(!(fragment instanceof AlbumSongs))
        list.add("Edit tags");
        listView.setAdapter(new ArrayAdapter<String>(fragment.getContext(),R.layout.list_view,R.id.textview3,list));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(position==0)
        {
            AddToPlaylist addToPlaylist = null;
            if(fragment instanceof SongTab)
            addToPlaylist=new AddToPlaylist(fragment,(SongTab)fragment);
            else if(fragment instanceof AlbumSongs)
                addToPlaylist=new AddToPlaylist(fragment,(AlbumSongs)fragment);
            addToPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addToPlaylist.show();
            dismiss();
        }

        else if(position==2)
        {
            EditTags editTags=new EditTags(fragment,cursor);
            editTags.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            editTags.show();
            dismiss();
        }
        else if(position==3)
        {
            String id1=selectedItem.getItemSelected().get(0);
            ContentValues contentValues=new ContentValues();
            contentValues.put("_data", songs.getPath(id1));
            contentValues.put("title", songs.getTitle(id1));
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("_size", (new File(songs.getPath(id1)).length()));
            contentValues.put("artist", songs.getArtistForSong(id1));
            contentValues.put("is_ringtone", true);
            contentValues.put("album",songs.getAlbum(id1));
            contentValues.put("is_notification", false);
            contentValues.put("is_alarm", false);
            contentValues.put("is_music", true);
           /* contentValues.put("is_ringtone", true);
            contentValues.put("is_music", true);*/

            //songTab.getActivity().getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,contentValues,MediaStore.Audio.Media._ID+"=?",new String[]{songTab.getItemSelected().get(0)});
           Uri uri=MediaStore.Audio.Media.getContentUriForPath(songs.getPath(selectedItem.getItemSelected().get(0)));
            fragment.getActivity().getContentResolver().delete(uri,MediaStore.MediaColumns.DATA + "=\"" + songs.getPath(selectedItem.getItemSelected().get(0)) + "\"", null);
            Uri newUri=fragment.getActivity().getContentResolver().insert(uri,contentValues);
            RingtoneManager.setActualDefaultRingtoneUri(fragment.getActivity().getApplicationContext(),RingtoneManager.TYPE_RINGTONE,newUri);
            /*Settings.System.putString(songTab.getActivity().getContentResolver(), Settings.System.RINGTONE,
                    songTab.songs.getPath(selectedItem.getItemSelected().get(0)));*/
           /* RingtonePreference preference=new RingtonePreference(getContext());
            preference.setDefaultValue(MediaStore.Audio.Media.getContentUriForPath(songTab.songs.getPath(selectedItem.getItemSelected().get(0))));
            preference.shouldCommit();*/
         /* if(fragment instanceof SongTab) {
                songTab.title_key = songs.getTitleKeys();
                songTab.arrayAdapter = new CustomArrayAdapter(fragment, R.layout.list_view2, songTab.title_key, songs.getSongs(), songs.getDurations(), songs.getAllArtists());
                songTab.listview.setAdapter(songTab.arrayAdapter);
            }
            else if(fragment instanceof PlaylistSong)
            {
                playlistSong.listViewUpdate();
            }*/
            Toast.makeText(getContext(), "Ringtone added successfully", Toast.LENGTH_SHORT).show();
            dismiss();
        }

        else if(position==1)
        {
            if(cursor.moveToFirst()) {
                File file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("audio/mpeg");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    fragment.getActivity().startActivity(intent);
                } catch (Exception e) {
                    //Toast.makeText(MainActivity.this, " " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
            dismiss();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(cursor!=null)
            cursor.close();
    }
}
