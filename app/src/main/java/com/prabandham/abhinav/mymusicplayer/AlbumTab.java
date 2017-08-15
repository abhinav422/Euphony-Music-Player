package com.prabandham.abhinav.mymusicplayer;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by Abhinav on 8/30/2016.
 */
public class AlbumTab extends Fragment implements AdapterView.OnItemClickListener {
    GridView gridView;
    MainActivity mainActivity;
    Albums albums;
    int position;
    FragmentTransaction ft;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.album_tab, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       gridView=(GridView)view.findViewById(R.id.gridView);
        mainActivity=(MainActivity)getActivity();
        albums=mainActivity.saagp;
       // album.filter(album.context,album.uri,album.projection,null,null,MediaStore.Audio.Albums.DEFAULT_SORT_ORDER,MediaStore.Audio.Albums.ALBUM_ART);
        /*gridView.setAdapter(new ImageAdapter(getContext(),getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Albums.ALBUM)));
        gridView.setOnItemClickListener(AlbumTab.this);*/
       // setGridView();
       // Toast.makeText(getContext(),"on view created album tab",Toast.LENGTH_SHORT).show();
        Cursor cursor=getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Albums.ALBUM);

        gridView.setAdapter(new ImageAdapter(getContext(),cursor));
        gridView.setOnItemClickListener(AlbumTab.this);
    }

    private void setGridView() {
        new AsyncTask<ContentResolver,String,Cursor>(){

            @Override
            protected Cursor doInBackground(ContentResolver... params) {
                return params[0].query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Albums.ALBUM);

            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                gridView.setAdapter(new ImageAdapter(getContext(),cursor));
                gridView.setOnItemClickListener(AlbumTab.this);
            }
        }.execute(mainActivity.getContentResolver());

    }

    public void gridViewChange()
    {
      setGridView();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position=position;
        AlbumSongs albumSongs=AlbumSongs.getInstance((String)gridView.getAdapter().getItem(position));
        ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.replace(R.id.mainrl,albumSongs).addToBackStack(null).commit();


    }

}

