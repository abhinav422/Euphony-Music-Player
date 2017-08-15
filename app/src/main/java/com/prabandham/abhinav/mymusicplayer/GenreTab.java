package com.prabandham.abhinav.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
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
 * Created by Abhinav on 27-Dec-16.
 */
public class GenreTab extends Fragment implements AdapterView.OnItemClickListener {
    ListView listView;
    MainActivity mainActivity;
    FragmentTransaction ft;
    String[] STAR = { MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME };
    SAAGP saagp;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlist_tab, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView=(ListView)view.findViewById(R.id.listView6);
        mainActivity=(MainActivity) getActivity();
        saagp=mainActivity.saagp;
        String query = " audio_genres._id in (select genre_id from audio_genres_map where audio_id in (select _id from audio_meta where is_music != 0))" ;

        Cursor cursor=mainActivity.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,STAR,query,null,MediaStore.Audio.Genres.NAME);

        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_view, cursor,
                new String[]{MediaStore.Audio.Genres.NAME}, new int[]{R.id.textview3}, 1);

        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(this);
    }

    private void setListViewAdapter() {

        new AsyncTask<ContentResolver, Void, Cursor>() {

            @Override
            protected void onPostExecute(Cursor cursor) {


                    simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_view, cursor,
                            new String[]{MediaStore.Audio.Genres.NAME}, new int[]{R.id.textview3}, 1);

                    listView.setAdapter(simpleCursorAdapter);


            }

            @Override
            protected Cursor doInBackground(ContentResolver... params) {
                String query = " audio_genres._id in (select genre_id from audio_genres_map where audio_id in (select _id from audio_meta where is_music != 0))" ;

                return params[0].query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,STAR,query,null,MediaStore.Audio.Genres.NAME);
            }
        }.execute(mainActivity.getContentResolver());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c=((SimpleCursorAdapter)listView.getAdapter()).getCursor();
        c.moveToPosition(position);
        ArtistSong artistSong=ArtistSong.getInstance(c.getString(c.getColumnIndex(MediaStore.Audio.Genres._ID)),c.getString(c.getColumnIndex(MediaStore.Audio.Genres.NAME)),this);
        ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.replace(R.id.mainrl,artistSong).addToBackStack(null).commit();
    }
}
