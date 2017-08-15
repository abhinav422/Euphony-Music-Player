package com.prabandham.abhinav.mymusicplayer;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abhinav on 16-Jan-17.
 */
public class PlaylistSong extends Fragment implements AdapterView.OnItemClickListener,SelectedItem, AdapterView.OnItemLongClickListener {
    ListView listView;
    MainActivity mainActivity;
    Songs songs;
    String playlist_id;
    OnClickInFragment onClickInFragment;
    TextView textView;
    String playlistName;
    ArrayList<String> item_selected;
    SimpleCursorAdapter simpleCursorAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlist_song, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initialize(view);
        mainActivity=(MainActivity)getActivity();
        onClickInFragment=mainActivity;
        songs=mainActivity.saagp;
        playlistName=getArguments().getString("name");
        playlist_id=getArguments().getString("id");
        textView.setText(playlistName);
        setListViewAdapter();
        mainActivity.saagp.setSortOrder(MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        listView.setOnItemClickListener(this);
       // listView.setOnItemLongClickListener(this);

    }

    private void setListViewAdapter() {

        if(playlist_id.equals("Recently Added"))
        {
            simpleCursorAdapter=new SimpleCursorAdapter(getContext(),R.layout.list_view,getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DATE_ADDED),
                    new String[]{MediaStore.Audio.Media.TITLE},new int[]{R.id.textview3},1);
        }
        else
        {
            simpleCursorAdapter=new SimpleCursorAdapter(getContext(),R.layout.list_view,getActivity().getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.valueOf(playlist_id)),null,null,null,MediaStore.Audio.Playlists.Members.PLAY_ORDER),
                    new String[]{MediaStore.Audio.Playlists.Members.TITLE},new int[]{R.id.textview3},1);
        }

        listView.setAdapter(simpleCursorAdapter);
    }

    private void initialize(View view)
    {
        listView=(ListView) view.findViewById(R.id.listView6);
        textView=(TextView)view.findViewById(R.id.textView12);

        textView.setSelected(true);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onClickInFragment.initiateSong(((SimpleCursorAdapter)listView.getAdapter()).getCursor(),position);
    }

    public void listViewUpdate()
    {
       setListViewAdapter();
    }
    @Override
    public ArrayList<String> getItemSelected() {
        return item_selected;
    }

    @Override
    public void setItemSelected(ArrayList<String> item_selected) {

        this.item_selected=item_selected;
    }

    @Override
    public void setItemSelected(String item_selected) {
        this.item_selected=new ArrayList<>();
        this.item_selected.add(item_selected);
    }
    @Override
    public void createPlaylist()
    {
        CreatePlaylist createPlaylist=new CreatePlaylist(this,this);
        createPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createPlaylist.show();
    }

    @Override
    public Cursor getCursor() {
        return null;
    }

    @Override
    public void setCursor(Cursor cursor) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AsyncTask<Cursor,Void,MatrixCursor>()
        {

            @Override
            protected void onPostExecute(MatrixCursor matrixCursor)
            {
                if(matrixCursor.getCount()>0)
                {
                    SongOptions songOptions=new SongOptions(PlaylistSong.this,matrixCursor);
                    songOptions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    songOptions.show();

                }
            }

            @Override
            protected MatrixCursor doInBackground(Cursor... params) {
                MatrixCursor matrixCursor = new MatrixCursor(params[0].getColumnNames());
                if(params[0].moveToPosition(position)) {

                    ArrayList<Object> x = new ArrayList<Object>();
                    for (int i = 0; i < matrixCursor.getColumnCount(); i++) {
                        x.add(params[0].getString(params[0].getColumnIndex(matrixCursor.getColumnName(i))));
                    }
                    matrixCursor.addRow(x.toArray());
                }
                return matrixCursor;
            }
        }.execute(((SimpleCursorAdapter)listView.getAdapter()).getCursor());
        return true;
    }

    public static PlaylistSong getInstance(String id, String s) {
        PlaylistSong playlistSong=new PlaylistSong();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("name",s);
        playlistSong.setArguments(bundle);
        return playlistSong;
    }
}
