package com.prabandham.abhinav.mymusicplayer;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Abhinav on 04-Jan-17.
 */
public interface Genres {
    Uri uri= MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    String[] projection={MediaStore.Audio.Genres.Members._ID,MediaStore.Audio.Genres.NAME};
    String sortOrder=MediaStore.Audio.Genres.DEFAULT_SORT_ORDER;
    ArrayList<String> getGenres();
    String getGenreId(String x);
}
