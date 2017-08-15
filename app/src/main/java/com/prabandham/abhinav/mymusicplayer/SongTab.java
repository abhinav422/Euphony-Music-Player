package com.prabandham.abhinav.mymusicplayer;


import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SearchViewCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by Abhinav on 8/30/2016.
 */
public class SongTab extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener,SortOrderInterface,SelectedItem, android.support.v7.widget.SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener {

    ListView listview;
    SAAGP saagp;
    MainActivity mainActivity;
    OnClickInFragment onClickInFragment;
    ImageButton imageButton;
    ImageButton multiselect;
    ImageButton shuffle;
    //CustomArrayAdapter arrayAdapter;
    ImageButton multiselect_back;
    ImageButton multiselect_addtoplaylist;
    //ArrayList<String> title_key;
    ImageButton multiselect_play;
    ImageButton search;
    ImageButton settings;
    ImageButton delete;
    ArrayList<String> item_selected;
    ViewFlipper viewFlipper;
    android.support.v7.widget.SearchView searchView;
    CustomCursorAdapter customCursorAdapter;
    Cursor selectedCursor;
    String sortOrder=SortOrderInterface.sortOrder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.song_tab, container, false);
        Log.v("songtab","oncreate view");
        return rootView;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.v("songtab","on resume");
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.v("songtab","on pause");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.v("songtab","onview created");
        initialize(view);
        mainActivity=(MainActivity) getActivity();
        onClickInFragment=mainActivity;
        this.saagp=mainActivity.saagp;
        Cursor cursor=getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.IS_MUSIC+"!=0"+ " AND "+MediaStore.Audio.Media.DURATION+" >= "+saagp.getThDuration() ,null,sortOrder);
        customCursorAdapter=new CustomCursorAdapter(getContext(),R.layout.list_view2,cursor,
                new String[]{MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION},new int[]{R.id.textview3,R.id.textView7,R.id.textView8},1);
        listview.setAdapter(customCursorAdapter);
        setFilterQueryProvider();


    }

    private void initialize(View view)
    {
        imageButton=(ImageButton) view.findViewById(R.id.imageButton6);
        search=(ImageButton)view.findViewById(R.id.imageButton5);
        settings=(ImageButton)view.findViewById(R.id.imageButton14);
        multiselect=(ImageButton)view.findViewById(R.id.imageButton7);
        shuffle=(ImageButton)view.findViewById(R.id.imageButton8);
        viewFlipper=(ViewFlipper)view.findViewById(R.id.viewFlipper2);
        multiselect_play=(ImageButton)viewFlipper.findViewById(R.id.imageButton10);
        multiselect_addtoplaylist=(ImageButton)viewFlipper.findViewById(R.id.imageButton12);
        delete=(ImageButton)viewFlipper.findViewById(R.id.imageButton13);
        multiselect_back =(ImageButton)viewFlipper.findViewById(R.id.imageButton11);
        listview=(ListView)view.findViewById(R.id.listView);
        searchView= (android.support.v7.widget.SearchView) viewFlipper.findViewById(R.id.searchView);
        imageButton.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        multiselect.setOnClickListener(this);
        search.setOnClickListener(this);
        settings.setOnClickListener(this);
        multiselect_back.setOnClickListener(this);
        multiselect_addtoplaylist.setOnClickListener(this);
       // delete.setOnClickListener(this);
        multiselect_play.setOnClickListener(this);
        listview.setTextFilterEnabled(true);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setItemsCanFocus(true);
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
        selectedCursor=cursor;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Cursor cursor= ((CustomCursorAdapter)listview.getAdapter()).getCursor();
        onClickInFragment.initiateSong(cursor,position);
        //onClickInFragment.startFragment((ArrayList<String>) ((CustomArrayAdapter)listview.getAdapter()).title_keys,position,this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageButton6)
        {

            SortDialog sortDialog=new SortDialog(this,this);
            sortDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sortDialog.show();
        }
       else if(v.getId()==R.id.imageButton5)
        {
            SearchManager searchManager = (SearchManager)getActivity(). getSystemService(Context.SEARCH_SERVICE);
            viewFlipper.setDisplayedChild(1);
            searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    viewFlipper.showPrevious();
                    return true;
                }
            });

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            searchView.setOnQueryTextListener((android.support.v7.widget.SearchView.OnQueryTextListener) this);
            searchView.setIconifiedByDefault(true);
            searchView.setFocusable(true);
            searchView.performClick();
            searchView.setSelected(true);
            searchView.requestFocus();
        }
        else if(v.getId()==R.id.imageButton7)
        {
            Toast.makeText(getContext(),"multiselect on",Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(2);
            listview.setOnItemClickListener(null);
            listview.setOnItemLongClickListener(null);
            listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        }
        else if(v.getId()==R.id.imageButton8)
        {

            new AsyncTask<ContentResolver,Void,Cursor>()
            {
                @Override
                protected void onPostExecute(Cursor cursor) {
                    if(cursor.getCount()>0) {
                        onClickInFragment.initiateSong(cursor, 0);
                        Toast.makeText(getContext(), "shuffle", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected Cursor doInBackground(ContentResolver... params) {
                    return params[0].query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,"RANDOM()");
                }
            }.execute(mainActivity.getContentResolver());

        }
        else if(v.getId()==R.id.imageButton14)
        {
            mainActivity.startSetting();
        }
        else if(v.getId()==R.id.imageButton10)
        {

            final SparseBooleanArray checked=listview.getCheckedItemPositions();
            new AsyncTask<Cursor,Void,MatrixCursor>(){

                @Override
                protected void onPostExecute(MatrixCursor matrixCursor) {

                    if(matrixCursor.getCount()>0)
                    {
                        setCursor(matrixCursor);
                        onClickInFragment.initiateSong(matrixCursor,0);
                        listview.clearChoices();
                    }
                    else
                        Toast.makeText(getContext(),"No song is selected",Toast.LENGTH_SHORT).show();
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
            }.execute(((CustomCursorAdapter)listview.getAdapter()).getCursor());
        }
        else if(v.getId()==R.id.imageButton12)
        {
            final SparseBooleanArray checked=listview.getCheckedItemPositions();
            new AsyncTask<Cursor,Void,MatrixCursor>(){

                @Override
                protected void onPostExecute(MatrixCursor matrixCursor) {

                    if(matrixCursor.getCount()>0)
                    {
                        setCursor(matrixCursor);
                        AddToPlaylist addToPlaylist = new AddToPlaylist(SongTab.this,SongTab.this);
                        addToPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        addToPlaylist.show();
                        listview.clearChoices();
                    }
                    else
                        Toast.makeText(getContext(),"No song is selected",Toast.LENGTH_SHORT).show();
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
            }.execute(((CustomCursorAdapter)listview.getAdapter()).getCursor());

        }
        /*else if(v.getId()==R.id.imageButton13)
        {
            Toast.makeText(getContext(),"delete",Toast.LENGTH_SHORT).show();
            Log.v("deletesongtab","enter");
            final SparseBooleanArray checked=listview.getCheckedItemPositions();
            new AsyncTask<Cursor,Void,MatrixCursor>(){

                @Override
                protected void onPostExecute(MatrixCursor matrixCursor) {

                    Log.v("deletesongtab","enter2");
                    if(matrixCursor.moveToFirst())
                    {
                        do{
                            Log.v("deletesongtab","enter3");
                            mainActivity.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID+"=?",new String[]{matrixCursor.getString(matrixCursor.getColumnIndex(MediaStore.Audio.Media._ID))});
                            mainActivity.notifyViewPager();

                        }while(matrixCursor.moveToNext());
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
            }.execute(((CustomCursorAdapter)listview.getAdapter()).getCursor());
        }*/
        else if(v.getId()==R.id.imageButton11)
        {
            final ListView lv = listview;
            lv.clearChoices();
            for (int i = 0; i < lv.getCount(); i++)
                lv.setItemChecked(i, false);
            viewFlipper.setDisplayedChild(0);
            lv.post(new Runnable() {
                @Override
                public void run() {
                    lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    lv.setOnItemClickListener(SongTab.this);
                    lv.setOnItemLongClickListener(SongTab.this);
                }
            });

        }


        }

    public void setSortOrder(String sortOrder)
    {
        this.sortOrder=sortOrder;
    }


    public String getSortOrder() {
        return sortOrder;
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
    {
        new AsyncTask<Cursor,Void,MatrixCursor>()
        {

            @Override
            protected void onPostExecute(MatrixCursor matrixCursor)
            {
                if(matrixCursor.getCount()>0)
                {
                    setCursor(matrixCursor);
                    SongOptions songOptions=new SongOptions(SongTab.this,matrixCursor);
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

    public void notifyDataSetChanged() {
        Log.v("SongTab","notifydatasetchange");
        updateListViewAdapter();

    }

    @Override
    public CustomCursorAdapter getListViewAdapter() {
        return (CustomCursorAdapter)listview.getAdapter();
    }

    @Override
    public Cursor getNewCursor(String sortOrder) {
        return getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.IS_MUSIC+"!=0"+ " AND "+MediaStore.Audio.Media.DURATION+" >= "+saagp.getThDuration(),null,sortOrder);
    }

    public void updateListViewAdapter() {
        Log.v("songtab","update list view");

        new AsyncTask<ContentResolver, Void, Cursor>() {

            @Override
            protected void onPostExecute(Cursor cursor) {

                if(listview.getAdapter()!=null)
                {
                    ((CustomCursorAdapter) listview.getAdapter()).swapCursor(cursor);
                }
                else {
                    customCursorAdapter = new CustomCursorAdapter(getContext(), R.layout.list_view2, cursor,
                            new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION}, new int[]{R.id.textview3, R.id.textView7, R.id.textView8}, 1);

                    listview.setAdapter(customCursorAdapter);
                }
                setFilterQueryProvider();
            }

            @Override
            protected Cursor doInBackground(ContentResolver... params) {
                return params[0].query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.IS_MUSIC+"!=0"+ " AND "+MediaStore.Audio.Media.DURATION+" >= "+saagp.getThDuration(),null,sortOrder);
            }
        }.execute(mainActivity.getContentResolver());
    }

    private void setFilterQueryProvider() {
        customCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {

            @Override
            public Cursor runQuery(CharSequence constraint) {
                String partialValue = constraint.toString();
                Cursor c=mainActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.TITLE + " LIKE ?"+ " AND "+MediaStore.Audio.Media.DURATION+" >= "+saagp.getThDuration(),new String[]{"%"+partialValue+"%"},sortOrder);
                return c;
            }
        });
    }

}
