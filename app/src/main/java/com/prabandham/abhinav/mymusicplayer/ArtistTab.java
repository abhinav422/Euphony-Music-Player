package com.prabandham.abhinav.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
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

/**
 * Created by Abhinav on 02-Sep-16.
 */
public class ArtistTab extends Fragment implements AdapterView.OnItemClickListener {
    ListView listView;
    MainActivity mainActivity;
    SAAGP saagp;
    FragmentTransaction ft;
    SimpleCursorAdapter simpleCursorAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.artist_tab, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView=(ListView)view.findViewById(R.id.listView2);
        mainActivity=(MainActivity) getActivity();
        saagp=mainActivity.saagp;
        Cursor cursor=mainActivity.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Artists.ARTIST);
        simpleCursorAdapter=new SimpleCursorAdapter(getContext(),R.layout.list_view,cursor,
                new String[]{MediaStore.Audio.Artists.ARTIST},new int[]{R.id.textview3},1);

        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(this);
    }

    private void setListViewAdapter() {
        new AsyncTask<ContentResolver, Void, Cursor>() {

            @Override
            protected void onPostExecute(Cursor cursor) {
                simpleCursorAdapter=new SimpleCursorAdapter(getContext(),R.layout.list_view,cursor,
                        new String[]{MediaStore.Audio.Artists.ARTIST},new int[]{R.id.textview3},1);

                listView.setAdapter(simpleCursorAdapter);

            }

            @Override
            protected Cursor doInBackground(ContentResolver... params) {
                return params[0].query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Artists.ARTIST);
            }
        }.execute(mainActivity.getContentResolver());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c=((SimpleCursorAdapter)listView.getAdapter()).getCursor();
        c.moveToPosition(position);
        ArtistSong artistSong=ArtistSong.getInstance(c.getString(c.getColumnIndex(MediaStore.Audio.Artists._ID)),c.getString(c.getColumnIndex(MediaStore.Audio.Artists.ARTIST)),this);
        ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.replace(R.id.mainrl,artistSong).addToBackStack(null).commit();
    }
}
