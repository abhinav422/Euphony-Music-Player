package com.prabandham.abhinav.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Abhinav on 05-Oct-16.
 */
public class AlbumSongs extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener,SortOrderInterface,SelectedItem, SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener {
    ImageView imageView;
    ListView listView;
    Songs songs;
    MainActivity mainActivity;
    ImageButton sort;
    ImageButton search;
    ImageButton shuffle;
    ImageButton multiselect_back;
    ImageButton multiselect_addtoplaylist;
    ImageButton multiselect_play;
    ImageButton multiselect;
    SearchView searchView;
    TextView textView;
    TextView textView2;
    TextView textView3;
    ArrayList<String> item_selected;
    String sortOrder=SortOrderInterface.sortOrder;
    OnClickInFragment onClickInFragment;
    String album_key;
    Long duration=0L;
    ViewFlipper viewFlipper;
    private ArrayList<String> songsForAlbum;
    CustomCursorAdapter customCursorAdapter;
    private Cursor selectedCursor;
    CustomTransformation customTransformation;
    public static AlbumSongs getInstance(String album_key)
    {
        AlbumSongs albumSongs=new AlbumSongs();
        Bundle bundle=new Bundle();
        bundle.putString("albumkey",album_key);
        albumSongs.setArguments(bundle);
        return albumSongs;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.album_song, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initialize(view);
        songs=mainActivity.saagp;
        this.album_key=getArguments().getString("albumkey");
        mainActivity=(MainActivity) getActivity();
        onClickInFragment=mainActivity;
        songs=mainActivity.saagp;
        Picasso.with(getContext()).load(new File(songs.getAlbumArt(album_key)+"")).transform(customTransformation).placeholder(R.drawable.musicicon1).resize(200,200).onlyScaleDown().into(imageView);
        setListViewAdapter();

        textView.setText(songs.getAlbumById(album_key));
        textView.setSelected(true);




    }

    private void setListViewAdapter() {

        new AsyncTask<ContentResolver, Void, Cursor>() {

            @Override
            protected void onPostExecute(Cursor cursor) {
                customCursorAdapter=new CustomCursorAdapter(getContext(),R.layout.list_view2,cursor,
                        new String[]{MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION},new int[]{R.id.textview3,R.id.textView7,R.id.textView8},1);
                listView.setAdapter(customCursorAdapter);
               setFilterQueryProvider();
                textView2.setText("Total Songs : "+new Integer(customCursorAdapter.getCount()).toString());
                textView3.setText("Total Duration : "+String.format("%02d:%02d", ((duration/1000)/60), (00+((duration/1000)%60))));
            }

            @Override
            protected Cursor doInBackground(ContentResolver... params) {
                Cursor cursor= params[0].query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.ALBUM_KEY+"=?",new String[]{album_key},sortOrder);
                if(cursor.moveToFirst())
                {
                    do{
                        duration=duration + Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    }while(cursor.moveToNext());
                }
                return cursor;
            }
        }.execute(mainActivity.getContentResolver());
    }

    private void setFilterQueryProvider() {
        customCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String partialValue = constraint.toString();
                Cursor c=mainActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.ALBUM_KEY+"=?" +" AND "+MediaStore.Audio.Media.TITLE + " LIKE ?",new String[]{album_key,"%"+partialValue+"%"},sortOrder);
                return c;
            }
        });
    }

    private void initialize(View view)
    {
        imageView=(ImageView)view.findViewById(R.id.imageView3);
        listView=(ListView)view.findViewById(R.id.listView3);
        textView=(TextView)view.findViewById(R.id.textView10);
        textView2=(TextView)view.findViewById(R.id.textView18);
        textView3=(TextView)view.findViewById(R.id.textView19);
        viewFlipper=(ViewFlipper)view.findViewById(R.id.viewFlipper2);
        sort=(ImageButton)view.findViewById(R.id.imageButton6);
        shuffle=(ImageButton)view.findViewById(R.id.imageButton8);
        multiselect=(ImageButton)view.findViewById(R.id.imageButton7);
        search=(ImageButton)view.findViewById(R.id.imageButton5);
        searchView=(SearchView)viewFlipper.findViewById(R.id.searchView);
        multiselect_play = (ImageButton) viewFlipper.findViewById(R.id.imageButton10);
        multiselect_addtoplaylist = (ImageButton) viewFlipper.findViewById(R.id.imageButton12);
        multiselect_back = (ImageButton) viewFlipper.findViewById(R.id.imageButton11);
        shuffle.setOnClickListener(this);

        multiselect.setOnClickListener(this);
        sort.setOnClickListener(this);

        search.setOnClickListener(this);

        multiselect_back.setOnClickListener(this);
        multiselect_addtoplaylist.setOnClickListener(this);
        multiselect_play.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        mainActivity=(MainActivity)getActivity();
        customTransformation=new CustomTransformation(200,200);
    }
    public String getAlbum_key()
    {
        return album_key;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onClickInFragment.initiateSong(getListViewAdapter().getCursor(),position);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton6) {
            SortDialog sortDialog = new SortDialog(this,this);
            sortDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sortDialog.show();
        } else if (v.getId() == R.id.imageButton5) {
            viewFlipper.showNext();
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    viewFlipper.showPrevious();
                    return true;
                }
            });
            searchView.setOnQueryTextListener(this);

        } else if (v.getId() == R.id.imageButton7) {
            Toast.makeText(getContext(), "multiselect on", Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(2);
            listView.setOnItemClickListener(null);
            listView.setOnItemLongClickListener(null);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        } else if (v.getId() == R.id.imageButton8) {
            new AsyncTask<ContentResolver,Void,Cursor>()
            {
                @Override
                protected void onPostExecute(Cursor cursor) {
                    onClickInFragment.initiateSong(cursor,0);
                }

                @Override
                protected Cursor doInBackground(ContentResolver... params) {
                    return params[0].query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.ALBUM_KEY+"=?" + " AND "+ MediaStore.Audio.Media.IS_MUSIC+"!=0",new String[]{album_key},"RANDOM()");
                }
            }.execute(mainActivity.getContentResolver());
        } else if (v.getId() == R.id.imageButton10) {

            final SparseBooleanArray checked=listView.getCheckedItemPositions();
            new AsyncTask<Cursor,Void,MatrixCursor>(){

                @Override
                protected void onPostExecute(MatrixCursor matrixCursor) {

                    if(matrixCursor.getCount()>0)
                    {
                        setCursor(matrixCursor);
                        onClickInFragment.initiateSong(matrixCursor,0);
                        listView.clearChoices();
                    }
                }

                @Override
                protected MatrixCursor doInBackground(Cursor... params) {
                    Cursor c=params[0];
                    final MatrixCursor matrixCursor=new MatrixCursor(c.getColumnNames());
                    for(int i=0;i<c.getCount();i++)
                    {
                        ArrayList<Object> a=new ArrayList<Object>();
                        if(checked.get(i)) {

                            c.moveToPosition(i);
                            if(!a.isEmpty())
                            {
                                a.clear();
                            }
                            for(int j=0;j<matrixCursor.getColumnCount();j++)
                            {
                                a.add(c.getString(c.getColumnIndex(matrixCursor.getColumnName(j))));
                            }
                            matrixCursor.addRow(a.toArray());
                        }
                    }

                    return matrixCursor;
                }
            }.execute(((CustomCursorAdapter)listView.getAdapter()).getCursor());
        }
        else if (v.getId() == R.id.imageButton11) {
            final ListView lv = listView;
            lv.clearChoices();
            for (int i = 0; i < lv.getCount(); i++)
                lv.setItemChecked(i, false);
            viewFlipper.setDisplayedChild(0);
            lv.post(new Runnable() {
                @Override
                public void run() {
                    lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    lv.setOnItemClickListener(AlbumSongs.this);
                    lv.setOnItemLongClickListener(AlbumSongs.this);
                }
            });
        }
        else if (v.getId() == R.id.imageButton12)
        {
            final SparseBooleanArray checked=listView.getCheckedItemPositions();
            new AsyncTask<Cursor,Void,MatrixCursor>(){

                @Override
                protected void onPostExecute(MatrixCursor matrixCursor) {

                    if(matrixCursor.getCount()>0)
                    {
                        setCursor(matrixCursor);
                        AddToPlaylist addToPlaylist = new AddToPlaylist(AlbumSongs.this,AlbumSongs.this);
                        addToPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        addToPlaylist.show();
                        listView.clearChoices();
                    }
                }

                @Override
                protected MatrixCursor doInBackground(Cursor... params) {
                    Cursor c=params[0];
                    final MatrixCursor matrixCursor=new MatrixCursor(c.getColumnNames());
                    for(int i=0;i<c.getCount();i++)
                    {
                        ArrayList<Object> a=new ArrayList<Object>();
                        if(checked.get(i)) {

                            c.moveToPosition(i);
                            if(!a.isEmpty())
                            {
                                a.clear();
                            }
                            for(int j=0;j<matrixCursor.getColumnCount();j++)
                            {
                                a.add(c.getString(c.getColumnIndex(matrixCursor.getColumnName(j))));
                            }
                            matrixCursor.addRow(a.toArray());
                        }
                    }

                    return matrixCursor;
                }
            }.execute(((CustomCursorAdapter)listView.getAdapter()).getCursor());
        }
    }

    @Override
    public void setSortOrder(String sortOrder) {
        this.sortOrder=sortOrder;
    }

    @Override
    public String getSortOrder() {
        return sortOrder;
    }

    @Override
    public CustomCursorAdapter getListViewAdapter() {
        return ((CustomCursorAdapter)listView.getAdapter());
    }

    @Override
    public Cursor getNewCursor(String sortOrder) {
        return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.ALBUM_KEY+"=?" +" AND "+ MediaStore.Audio.Media.IS_MUSIC+"!=0",new String[]{album_key},sortOrder);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getListViewAdapter().getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getListViewAdapter().getFilter().filter(newText);
        return true;
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
    public void onDestroy() {
        super.onDestroy();
        if(listView.getAdapter()!=null) {
            if (((CustomCursorAdapter) listView.getAdapter()).getCursor() != null)
                ((CustomCursorAdapter) listView.getAdapter()).getCursor().close();
        }
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
        return selectedCursor;
    }

    @Override
    public void setCursor(Cursor cursor) {

        this.selectedCursor=cursor;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
        new AsyncTask<Cursor,Void,MatrixCursor>()
        {

            @Override
            protected void onPostExecute(MatrixCursor matrixCursor)
            {
                if(matrixCursor.getCount()>0)
                {
                    setCursor(matrixCursor);
                    SongOptions songOptions=new SongOptions(AlbumSongs.this,matrixCursor);
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
        }.execute(getListViewAdapter().getCursor());
        return true;
    }
}
