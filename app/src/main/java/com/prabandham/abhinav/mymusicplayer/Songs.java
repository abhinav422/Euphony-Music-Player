package com.prabandham.abhinav.mymusicplayer;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Abhinav on 04-Jan-17.
 */
public interface Songs extends Albums,Genres,Playlists{
    Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    String[] projection={MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.TITLE_KEY,MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ALBUM_KEY,MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.ARTIST_ID,MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Media.BOOKMARK,MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.DATE_ADDED,MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.IS_ALARM, MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_PODCAST, MediaStore.Audio.Media.IS_RINGTONE,MediaStore.Audio.Media.SIZE,MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.BOOKMARK,MediaStore.Audio.Media._ID};
    String sortOrder=MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

    ArrayList<String> getTitleKeys();
    ArrayList<String> getSongs();
    ArrayList<String> getSongsForTitleKeys(ArrayList<String> title_key);
    ArrayList<String> getTitleKeysForSongs(ArrayList<String> songs);
    String getPath(String x);
    String getAlbumKeyForSong(String x);
    String getAlbum(String x);
    String getDuration(String x);
    String getArtistKey(String x);
    String getArtistForSong(String x);
    String getBookmark(String x);
    String getTitle(String x);
    ArrayList<String> getDurationForTitleKeys(ArrayList<String> title_key);
    ArrayList<String> getArtistsForTitleKeys(ArrayList<String> title_key);
    String getAlbumKeyForSongAndArtist(String x,String artist);
    ArrayList<String> getSongIds();
    ArrayList<String> getDurations();

    ArrayList<String> getAllArtists();
    void setSortOrder(String sortOrder);
    ArrayList<String> getTitlekeyForAlbumAndArtist(String album_key,String artist_id);
    String getSortOrder();
    ArrayList<String> getSongForAlbum(String x);
    ArrayList<String> getAlbumsForGenre(String id);

    String getSongId(String title_key);

}
