package com.prabandham.abhinav.mymusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * Created by Abhinav on 12-Jun-17.
 */

public class CustomCursorAdapter extends SimpleCursorAdapter implements CustomFilter.CursorFilterClient{


    LayoutInflater layoutInflater;
    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v=layoutInflater.inflate(R.layout.list_view2,parent,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView title=(TextView)view.findViewById(R.id.textview3);
        TextView artist=(TextView)view.findViewById(R.id.textView7);
        TextView duration=(TextView)view.findViewById(R.id.textView8);
        int title_index=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int artist_index=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int duration_index=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

        title.setText(cursor.getString(title_index)+"");
        artist.setText(cursor.getString(artist_index)+"");

        String duration2=cursor.getString(duration_index)+"";
        try{
            duration.setText(String.format("%02d:%02d",
                    ((Long.parseLong(duration2)/1000)/60),
                    (00+((Long.parseLong(duration2)/1000)%60))));
        }
        catch (Exception e)
        {
            duration.setText("00:00");
        }

    }

    @Override
    public Filter getFilter() {
      return new CustomFilter(this);
    }


}
