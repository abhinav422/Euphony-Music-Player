package com.prabandham.abhinav.mymusicplayer;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Abhinav on 04-Jan-17.
 */
public interface Albums extends Artists{
    Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    String[] projection={MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_KEY,MediaStore.Audio.Albums.ALBUM_ART,MediaStore.Audio.Albums.NUMBER_OF_SONGS,MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ARTIST
            };
    String sortOrder=MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;
    ArrayList<String> getAlbums();
    String getAlbumById(String album_id);
    ArrayList<String> getAlbumNameForAlbumKeys(ArrayList<String> album_keys);
    ArrayList<String> getAlbumArt();
    String getAlbumArt(String x);
    String getNoOfSongs(String x);
    ArrayList<String> getAlbumNames();
    String getNoOfSongsForArtist(String artistId);
    ArrayList<String> getAlbumKeysForArtist(String id);
    ArrayList<String> getAlbumArtForKeys(ArrayList<String> album_key);
}
