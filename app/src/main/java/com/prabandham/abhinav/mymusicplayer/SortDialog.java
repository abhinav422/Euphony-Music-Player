package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * Created by Abhinav on 22-Jan-17.
 */
public class SortDialog extends Dialog implements  RadioGroup.OnCheckedChangeListener {
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;
    RadioButton radioButton6;
    RadioGroup radioGroup;
    Songs songs;
    Fragment fragment;
    SortOrderInterface sortOrderInterface;
    MainActivity mainActivity;
    CustomCursorAdapter customCursorAdapter;
    ListView listView;
    Context context;
    public SortDialog(Fragment fragment,SortOrderInterface sortOrderInterface)
    {
        super(fragment.getContext());
        this.fragment=fragment;
        context=fragment.getContext();
        mainActivity=(MainActivity)fragment.getActivity();
       this.sortOrderInterface=sortOrderInterface;
        customCursorAdapter=sortOrderInterface.getListViewAdapter();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioButton1=(RadioButton)findViewById(R.id.radioButton);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);
        radioButton3=(RadioButton)findViewById(R.id.radioButton3);
        radioButton4=(RadioButton)findViewById(R.id.radioButton4);
        radioButton5=(RadioButton)findViewById(R.id.radioButton5);
        radioButton6=(RadioButton)findViewById(R.id.radioButton6);
        String s= sortOrderInterface.getSortOrder();
        if(s.equals(MediaStore.Audio.Media.TITLE))
        {
            radioGroup.check(R.id.radioButton);
        }
        else if(s.equals(MediaStore.Audio.Media.ALBUM))
        {
            radioGroup.check(R.id.radioButton2);
        }
        else if(s.equals(MediaStore.Audio.Media.ARTIST))
        {
            radioGroup.check(R.id.radioButton3);
        }
        else if(s.equals(MediaStore.Audio.Media.DURATION))
        {
            radioGroup.check(R.id.radioButton4);
        }
        else if(s.equals(MediaStore.Audio.Media.DATE_ADDED))
        {
            radioGroup.check(R.id.radioButton5);
        }
        else if(s.equals(MediaStore.Audio.Media.YEAR))
        {
            radioGroup.check(R.id.radioButton6);
        }
        radioGroup.setOnCheckedChangeListener(this);
        //radioButton1.setOnClickListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if(checkedId==R.id.radioButton)
        {
            sort(MediaStore.Audio.Media.TITLE);

        }
         else if(checkedId==R.id.radioButton2)
        {
            sort(MediaStore.Audio.Media.ALBUM);

        }
        else if(checkedId==R.id.radioButton3)
        {
            sort(MediaStore.Audio.Media.ARTIST);

        }
        else if(checkedId==R.id.radioButton4)
        {
            sort(MediaStore.Audio.Media.DURATION);

        }
        else if(checkedId==R.id.radioButton5)
        {
            sort(MediaStore.Audio.Media.DATE_ADDED);
        }
        else if(checkedId==R.id.radioButton6)
        {
            sort(MediaStore.Audio.Media.YEAR);
        }
        dismiss();
    }
    public void sort(String sortOrder)
    {

        Cursor cursor=sortOrderInterface.getNewCursor(sortOrder);
        //Cursor c=mainActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.IS_MUSIC+"!=0",null,sortOrder);
        customCursorAdapter.swapCursor(cursor);
        customCursorAdapter.notifyDataSetChanged();
        sortOrderInterface.setSortOrder(sortOrder);
    }
}

