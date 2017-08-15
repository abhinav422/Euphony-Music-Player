package com.prabandham.abhinav.mymusicplayer;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by Abhinav on 23-Jan-17.
 */
public interface SortOrderInterface {
     String sortOrder= MediaStore.Audio.Media.TITLE;
     void setSortOrder(String sortOrder);
    String getSortOrder();
    public CustomCursorAdapter getListViewAdapter();
    Cursor getNewCursor(String sortOrder);
}
