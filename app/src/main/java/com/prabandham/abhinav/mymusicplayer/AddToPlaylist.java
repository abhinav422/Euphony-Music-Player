package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Abhinav on 17-Jan-17.
 */
public class AddToPlaylist extends Dialog implements AdapterView.OnItemClickListener {

    ListView listView;
    MainActivity mainActivity;
    Songs songs;
   // private Fragment fragment;
    SelectedItem selectedItem;
    ArrayList<String> adapter;
    public AddToPlaylist(Fragment fragment,SelectedItem selectedItem) {
        super(fragment.getContext());
        //this.fragment=fragment;
        mainActivity=(MainActivity)fragment.getActivity();
        this.selectedItem=selectedItem;
        songs=mainActivity.saagp;

    }
    public AddToPlaylist(MainActivity mainActivity)
    {
        super(mainActivity);
        this.mainActivity=mainActivity;
        selectedItem=mainActivity;
        songs=mainActivity.saagp;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.add_to_playlist,null);
        setContentView(view);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        listView=(ListView)findViewById(R.id.listviewdialog1);
        adapter=songs.getName();
        adapter.add("Now Playing");
        adapter.add("Create new playlist");
        listView.setAdapter(new ArrayAdapter<String>(mainActivity.getApplicationContext(),R.layout.list_view,R.id.textview3,adapter));
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    public void dismiss() {

        super.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(position==adapter.size()-2)
        {
            //mainActivity.addSong(selectedItem.getItemSelected());
            if(mainActivity.viewPager.getAdapter()!=null)
            ((ViewPagerAdapter)mainActivity.viewPager.getAdapter()).addCursor(selectedItem.getCursor());
            else
                Toast.makeText(getContext(),"There is no song currently being played",Toast.LENGTH_SHORT).show();


        }
        else if(position==adapter.size()-1)
        {

            selectedItem.createPlaylist();

        }
        else {
            if(addToPlaylist( songs.getId(adapter.get(position)))==1)
            {
                Toast.makeText(getContext(),Integer.valueOf(selectedItem.getCursor().getCount()).toString()+" songs added successfully to playlist",Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(getContext(),"Unable to add song to specified playlist",Toast.LENGTH_SHORT).show();

            }
        }
        this.dismiss();
    }
    public int addToPlaylist(String id)
    {
        try {

            MergeCursor cursor=new MergeCursor(new Cursor[]{selectedItem.getCursor()});

            if(cursor!=null && cursor.moveToFirst())
            {
                do{
                    ContentValues contentValues = new ContentValues();
                    String song_id= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    int size = songs.getSongidForPlaylist(id).size();
                    contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, size + 1);
                    contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song_id);
                    mainActivity.getContentResolver().insert(MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(id)), contentValues);

                }while(cursor.moveToNext());
                cursor.close();
            }
            else {
                if(cursor!=null)
                cursor.close();
            }

            return 1;
        }catch (Exception e)
        {
            return 0;
        }

    }
}
