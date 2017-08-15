package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Abhinav on 13-Apr-17.
 */
public class EditTags extends Dialog implements View.OnClickListener {

    SongTab songTab;
    PlaylistSong playlistSong;
    SAAGP saagp;
    String song_id;
    EditText editText;
    EditText editText2;
    EditText editText3;
    Button button;
    Fragment fragment;
    MainActivity mainActivity;
    Cursor cursor;
    public EditTags(Fragment fragment,Cursor cursor) {
        super(fragment.getContext());
        this.fragment=fragment;
        this.cursor=cursor;
        cursor.moveToFirst();
        if(fragment instanceof PlaylistSong)
        {
            this.song_id=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
        }
        else {
            this.song_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        }
        mainActivity = (MainActivity) fragment.getActivity();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.edittags,null);
        // view.setBackgroundResource(R.drawable.nature2);
        setContentView(view);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        editText=(EditText)findViewById(R.id.editText1);
        editText2=(EditText)findViewById(R.id.editText2);
        editText3=(EditText)findViewById(R.id.editText3);
        cursor.moveToFirst();
        editText.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        editText2.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        editText3.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button1)
        {
            ContentValues contentValues=new ContentValues();
            int c=0;
            cursor.moveToFirst();
            if (editText.getText().toString().equals("") || editText.getText().toString().equals(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))))
            {

            }
            else {
                contentValues.put(MediaStore.Audio.Media.TITLE, editText.getText().toString());
                c++;
            }

            if(editText2.getText().toString().equals("") || editText2.getText().toString().equals(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))))
            {

            }
            else {
                contentValues.put(MediaStore.Audio.Media.ALBUM, editText2.getText().toString());
                c++;
            }
            if(editText3.getText().toString().equals("") || editText3.getText().toString().equals(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))))
            {

            }
            else {
                contentValues.put(MediaStore.Audio.Media.ARTIST, editText3.getText().toString());
                c++;
            }
            if(c>0) {
                mainActivity.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues, MediaStore.Audio.Media._ID + "=?", new String[]{song_id});
                if (fragment instanceof SongTab) {
                    Cursor cursor=mainActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.IS_MUSIC+"!=0",null,((SongTab)fragment).getSortOrder());
                    ((SongTab)fragment).getListViewAdapter().swapCursor(cursor);
                    AlbumTab albumTab = (AlbumTab) mainActivity.fragmentParent.customPagerAdapter.instantiateItem(mainActivity.fragmentParent.viewpager, 1);
                    albumTab.gridViewChange();
                } else if (fragment instanceof PlaylistSong) {
                    playlistSong.listViewUpdate();
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
