package com.prabandham.abhinav.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import java.util.ArrayList;
import java.util.Collections;



/**
 * Created by Abhinav on 05-Nov-16.
 */
public class ArtistSong extends Fragment implements AdapterView.OnItemClickListener,SortOrderInterface,SelectedItem, View.OnClickListener, SearchView.OnQueryTextListener {


    String artistName;
    Songs songs;
    VelocityViewPager viewPager;
    TextView textView;
    ImageButton multiselect;
    ImageButton search;
    ImageButton sort;
    ArrayList<String> item_selected;
    ImageButton multiselect_back;
    ImageButton multiselect_addtoplaylist;
    ImageButton multiselect_play;
    String sortOrder=SortOrderInterface.sortOrder;
    String artist_id;
    String fragment;
    ViewFlipper viewFlipper;
    MainActivity mainActivity;
    SearchView searchView;
    FrameLayout frameLayout;
    ListView listView;
    ImageButton shuffle;

    OnClickInFragment onClickInFragment;
    CustomCursorAdapter customCursorAdapter;
    private Cursor selectedCursor;

    public static ArtistSong getInstance(String artist_key, String name, Fragment fragment) {

        ArtistSong artistSong=new ArtistSong();
        Bundle bundle=new Bundle();
        bundle.putString("artistid",artist_key);
        if(fragment instanceof ArtistTab) {
            bundle.putString("fragment", "artisttab");
        }
        else if(fragment instanceof GenreTab) {
            bundle.putString("fragment", "genretab");
        }
        bundle.putString("artist",name);
        artistSong.setArguments(bundle);
        return artistSong;
    }

    public Object getFragment() {
        return fragment;
    }

    public void setFragment(Object fragment) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.artist_song,null);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
       // featureCoverFlow=(FeatureCoverFlow)view.findViewById(R.id.coverflow);
       // fancyCoverFlow=(FancyCoverFlow)view.findViewById(R.id.fcf);
        initialize(view);
        artist_id =getArguments().getString("artistid");
        fragment=getArguments().getString("fragment");
        mainActivity=(MainActivity)getActivity();
        songs=mainActivity.saagp;
        onClickInFragment=mainActivity;
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer(true));
        viewPager.setClipChildren(false);
        viewPager.setPageMargin(0);
        new AsyncTask<String,Void,CustomImagePagerAdapter>()
        {

            @Override
            protected void onPostExecute(CustomImagePagerAdapter customImagePagerAdapter) {
                viewPager.setAdapter(customImagePagerAdapter);
                viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
                viewPager.setCurrentItem(0);
                setListViewAdapter(0);
            }

            @Override
            protected CustomImagePagerAdapter doInBackground(String... params) {
                if(fragment.equals("artisttab")) {
                    Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Artists.Albums.getContentUri("external", Long.valueOf(artist_id)), null, null, null, MediaStore.Audio.AlbumColumns.ALBUM);
                    return new CustomImagePagerAdapter(getChildFragmentManager(),c,0);
                }else {
                    Cursor c= getActivity().getContentResolver().query(MediaStore.Audio.Genres.Members.getContentUri("external", Long.valueOf(artist_id)), new String[]{"DISTINCT " + MediaStore.Audio.Genres.Members.ALBUM_KEY, MediaStore.Audio.Genres.Members.ALBUM}, "album_key IS NOT NULL) GROUP BY (album_key", null, null);
                  ArrayList<String> x=new ArrayList<String>();
                    if(c.moveToFirst()) {
                        do {
                            x.add(songs.getAlbumArt(c.getString(c.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM_KEY))));
                        } while (c.moveToNext());
                    }
                    return new CustomImagePagerAdapter(getChildFragmentManager(),c,1,x);
                }
            }
        }.execute(artist_id);
        // viewPager.setOffscreenPageLimit(10);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() - viewPager.getRight() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        return true;
                    } else if (event.getRawX() - viewPager.getLeft() < 0) {
                        //Toast.makeText(getContext(),new Integer((int)event.getRawX()).toString(),Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        return true;
                    }
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
               setListViewAdapter(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        listView.setOnItemClickListener(this);


    }

    private void setViewPagerAdapter() {

        new AsyncTask<String,Void,CustomImagePagerAdapter>()
        {

            @Override
            protected void onPostExecute(CustomImagePagerAdapter customImagePagerAdapter) {
               viewPager.setAdapter(customImagePagerAdapter);
                viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
                viewPager.setCurrentItem(0);
                setListViewAdapter(0);
            }

            @Override
            protected CustomImagePagerAdapter doInBackground(String... params) {
                if(fragment.equals("artisttab")) {
                    Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Artists.Albums.getContentUri("external", Long.valueOf(artist_id)), null, null, null, MediaStore.Audio.AlbumColumns.ALBUM);
                    return new CustomImagePagerAdapter(getChildFragmentManager(),c,0);
                }else {
                    Cursor c= getActivity().getContentResolver().query(MediaStore.Audio.Genres.Members.getContentUri("external", Long.valueOf(artist_id)), new String[]{"DISTINCT " + MediaStore.Audio.Genres.Members.ALBUM_KEY, MediaStore.Audio.Genres.Members.ALBUM}, "album_key IS NOT NULL) GROUP BY (album_key", null, null);
                    return new CustomImagePagerAdapter(getChildFragmentManager(),c,1,songs);
                }
            }
        }.execute(artist_id);
    }

    private void setListViewAdapter(int position) {

        Cursor c=((CustomImagePagerAdapter)viewPager.getAdapter()).getCursor();
        if(c.moveToPosition(position)) {
            final String album_key=c.getString(c.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_KEY));
            new AsyncTask<String, Void, Cursor>() {

                @Override
                protected void onPostExecute(Cursor cursor) {

                    if(customCursorAdapter!=null)
                    {
                        customCursorAdapter.swapCursor(cursor);
                    }
                    else {
                        customCursorAdapter = new CustomCursorAdapter(getContext(), R.layout.list_view2, cursor,
                                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION}, new int[]{R.id.textview3, R.id.textView7, R.id.textView8}, 1);

                        listView.setAdapter(customCursorAdapter);
                    }
                    setFilterQueryProvider(album_key);

                }

                @Override
                protected Cursor doInBackground(String... params) {

                    if(fragment.equals("artisttab"))
                    {
                       return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ALBUM_KEY + "=?" + " AND "+MediaStore.Audio.Media.ARTIST_ID+"=?", new String[]{params[0],artist_id}, sortOrder);

                    }
                    return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ALBUM_KEY + "=?", new String[]{params[0]}, sortOrder);
                    }
            }.execute(album_key);
        }
    }

    private void setFilterQueryProvider(final String album_key) {

        customCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {

            @Override
            public Cursor runQuery(CharSequence constraint) {
                String partialValue = constraint.toString();
                Cursor c;
                if(fragment.equals("artisttab"))
                   c =mainActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.ALBUM_KEY+"=?"+" AND "+ MediaStore.Audio.Media.ARTIST_ID+"=?"+" AND "+MediaStore.Audio.Media.TITLE + " LIKE ?",new String[]{album_key,artist_id,"%"+partialValue+"%"},sortOrder);
                else
                 c=mainActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.ALBUM_KEY+"=?"+" AND "+MediaStore.Audio.Media.TITLE + " LIKE ?",new String[]{album_key,"%"+partialValue+"%"},sortOrder);
                return c;
            }
        });
    }

    private void initialize(View view)
    {
        viewPager=(VelocityViewPager)view.findViewById(R.id.pager);
        textView=(TextView)view.findViewById(R.id.textview2);
        listView=(ListView)view.findViewById(R.id.listView4);;
        setArtist(getArguments().getString("artist"));
        textView.setText(artistName);
        textView.setSelected(true);
        frameLayout=(FrameLayout)view.findViewById(R.id.pager_container);
        viewFlipper=(ViewFlipper)view.findViewById(R.id.viewFlipper2);
        search=(ImageButton)viewFlipper.findViewById(R.id.imageButton5);
        sort=(ImageButton)viewFlipper.findViewById(R.id.imageButton6);
        multiselect=(ImageButton)viewFlipper.findViewById(R.id.imageButton7);
        searchView=(SearchView)viewFlipper.findViewById(R.id.searchView);
        multiselect_play=(ImageButton)viewFlipper.findViewById(R.id.imageButton10);
        multiselect_addtoplaylist=(ImageButton)viewFlipper.findViewById(R.id.imageButton12);
        multiselect_back =(ImageButton)viewFlipper.findViewById(R.id.imageButton11);
        multiselect_back.setOnClickListener(this);
        multiselect_addtoplaylist.setOnClickListener(this);
        multiselect_play.setOnClickListener(this);
        search.setOnClickListener(this);
        sort.setOnClickListener(this);
        multiselect.setOnClickListener(this);
        shuffle=(ImageButton)view.findViewById(R.id.imageButton8);
        shuffle.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onClickInFragment.initiateSong(((CustomCursorAdapter)listView.getAdapter()).getCursor(),position);
    }

    public void setArtist(String s) {

        artistName=s;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageButton5)
        {
            viewFlipper.setDisplayedChild(1);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    viewFlipper.showPrevious();
                    return true;
                }
            });
            searchView.setOnQueryTextListener(this);
        }

       else if(v.getId()==R.id.imageButton6)
        {
            SortDialog sortDialog=new SortDialog(this,this);
            sortDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sortDialog.show();
        }
        else if(v.getId()==R.id.imageButton7)
        {
            Toast.makeText(getContext(),"multiselect on",Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(2);
            listView.setOnItemClickListener(null);
            listView.setOnItemLongClickListener(null);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        }
        else if(v.getId()==R.id.imageButton8)
        {
            new AsyncTask<String,Void,Cursor>()
            {
                @Override
                protected void onPostExecute(Cursor cursor) {
                    onClickInFragment.initiateSong(cursor,0);
                }

                @Override
                protected Cursor doInBackground(String... params) {
                    if(fragment.equals("artisttab"))
                        return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ALBUM_KEY + "=?" + " AND "+MediaStore.Audio.Media.ARTIST_ID+"=?", new String[]{params[0],params[1]}, "RANDOM()");

                    return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ALBUM_KEY + "=?" , new String[]{params[0]}, "RANDOM()");

                }
            }.execute(((CustomImagePagerAdapter)viewPager.getAdapter()).getAlbumKey(viewPager.getCurrentItem()),artist_id);
        }
        else if(v.getId()==R.id.imageButton10)
        {

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
        else if(v.getId()==R.id.imageButton11)
        {
            final ListView lv = listView;
            lv.clearChoices();
            for (int i = 0; i < lv.getCount(); i++)
                lv.setItemChecked(i, false);
            viewFlipper.setDisplayedChild(0);
            lv.post(new Runnable() {
                @Override
                public void run() {
                    lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    lv.setOnItemClickListener(ArtistSong.this);

                }
            });

        }
        else if(v.getId()==R.id.imageButton12)
        {
            final SparseBooleanArray checked=listView.getCheckedItemPositions();
            new AsyncTask<Cursor,Void,MatrixCursor>(){

                @Override
                protected void onPostExecute(MatrixCursor matrixCursor) {

                    if(matrixCursor.getCount()>0)
                    {
                        setCursor(matrixCursor);
                        AddToPlaylist addToPlaylist = new AddToPlaylist(ArtistSong.this,ArtistSong.this);
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
    public boolean onQueryTextSubmit(String query) {
        customCursorAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customCursorAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void setSortOrder(String sortOrder)
    {
        this.sortOrder=sortOrder;
    }

    @Override
    public String getSortOrder() {
        return sortOrder;
    }

    @Override
    public CustomCursorAdapter getListViewAdapter() {
        return (CustomCursorAdapter) listView.getAdapter();
    }

    @Override
    public Cursor getNewCursor(String sortOrder)
    {
        String album_key=((CustomImagePagerAdapter)viewPager.getAdapter()).getAlbumKey(viewPager.getCurrentItem());

        if(fragment.equals("artisttab"))
        {
            return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ALBUM_KEY + "=?" + " AND "+MediaStore.Audio.Media.ARTIST_ID+"=?", new String[]{album_key,artist_id}, sortOrder);

        }
        return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ALBUM_KEY + "=?", new String[]{album_key}, sortOrder);
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
    public void createPlaylist() {

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



}
