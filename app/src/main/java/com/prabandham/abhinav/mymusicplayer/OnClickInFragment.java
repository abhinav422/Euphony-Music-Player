package com.prabandham.abhinav.mymusicplayer;

import android.database.Cursor;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Abhinav on 11-Oct-16.
 */
public interface OnClickInFragment {
    public void initiateSong(Cursor cursor,int position);
}
