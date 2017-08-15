package com.prabandham.abhinav.mymusicplayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Abhinav on 18-Jan-17.
 */
public class CreatePlaylist extends AddToPlaylist implements View.OnClickListener {


    MainActivity mainActivity;
    EditText editText;
    Button button;
    SelectedItem selectedItem;
    ContentResolver contentResolver;
    public CreatePlaylist(Fragment fragment,SelectedItem selectedItem) {
        super(fragment,selectedItem);
        mainActivity=(MainActivity) fragment.getActivity();
        contentResolver=mainActivity.getContentResolver();
        this.selectedItem=selectedItem;
        songs=mainActivity.saagp;


    }
    public CreatePlaylist(MainActivity mainActivity)
    {
        super(mainActivity);
        this.mainActivity=mainActivity;
        selectedItem=mainActivity;
        contentResolver=mainActivity.getContentResolver();
        songs=mainActivity.saagp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.create_playlist,null);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().gravity=Gravity.CENTER_VERTICAL;
        setContentView(view);
        setContentView(R.layout.create_playlist);
        editText=(EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String x=editText.getText().toString();

        if(!"".equals(x)) {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(MediaStore.Audio.Playlists.NAME, x);
                contentResolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, contentValues);
                Log.v("content resolver", "inserted");
                if (addToPlaylist(songs.getId(x)) == 1) {
                    Log.v("create playlist ok", "created");
                    Toast.makeText(getContext(), "Playlist created", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            }
            this.dismiss();
        }
        else{
            Toast.makeText(getContext(), "Playlist cannot have empty name", Toast.LENGTH_SHORT).show();
        }

    }
}
