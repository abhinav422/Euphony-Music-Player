package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abhinav on 14-Apr-17.
 */
public class RenameDialog extends Dialog implements View.OnClickListener {
    EditText editText;
    Button button;
    PlaylistTab playlistTab;
    MainActivity mainActivity;
    TextView textView;
    String id;
    public RenameDialog(Fragment fragment,String id) {
        super(fragment.getContext());
        if(fragment instanceof PlaylistTab)
        playlistTab=(PlaylistTab)fragment;
        mainActivity=(MainActivity)playlistTab.getActivity();
        this.id=id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.create_playlist,null);
        setContentView(view);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().gravity= Gravity.CENTER_VERTICAL;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        editText=(EditText)findViewById(R.id.editText);
        editText.setText(mainActivity.saagp.getNameForId(id));
        textView=(TextView)findViewById(R.id.textView3) ;
        textView.setText("Rename");
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button)
        {

            ContentValues  contentValues=new ContentValues();
            int c=0;
            if(editText.getText().toString().equals(""))
            {

            }
            else{
                c++;
                contentValues.put(MediaStore.Audio.Playlists.NAME,editText.getText().toString());
            }
            if(c>0) {
                mainActivity.getContentResolver().update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, contentValues, MediaStore.Audio.Playlists._ID + "=?", new String[]{id});
                ArrayList<String> x = mainActivity.saagp.getName();
                x.add("Recently Added");
                playlistTab.playlist=x;
                playlistTab.arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_view, R.id.textview3, x);
                playlistTab.listView.setAdapter(playlistTab.arrayAdapter);
            }
            dismiss();
        }
    }
}
