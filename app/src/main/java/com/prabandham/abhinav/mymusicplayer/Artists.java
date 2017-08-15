package com.prabandham.abhinav.mymusicplayer;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Abhinav on 04-Jan-17.
 */
public interface Artists {
    Uri uri= MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    String[] projection={MediaStore.Audio.Artists._ID,MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    String sortOrder=MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;
    ArrayList<String> getArtists();
    ArrayList<String> getArtistKeys();
    String getArtist(String artistId);
    String getArtistKeyById(String artistId);
    String getArtistId(String x);
    String getCount(String x);
    String getNoOfAlbums(String x);

}
