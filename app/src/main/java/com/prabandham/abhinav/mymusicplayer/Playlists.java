package com.prabandham.abhinav.mymusicplayer;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Abhinav on 16-Jan-17.
 */
public interface Playlists {
    Uri uri= MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    String[] projection={MediaStore.Audio.Playlists.NAME,MediaStore.Audio.Playlists._ID};
    String[] membersProjection={MediaStore.Audio.Playlists.Members.AUDIO_ID,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE
            ,MediaStore.Audio.Media.TITLE_KEY,MediaStore.Audio.Media.ALBUM
            ,MediaStore.Audio.Media.ALBUM_KEY,MediaStore.Audio.Media.DATA
            ,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.ARTIST_KEY
            ,MediaStore.Audio.Playlists.Members.PLAY_ORDER};
    String sortOrder=MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER;
    ArrayList<String> getName();
    String getNameForId(String playlist_id);
    String getId(String playlist);
    ArrayList<String> getSongidForPlaylist(String id);
    ArrayList<String> getIdForPlaylistSong(ArrayList<String> song_id,String playlist_id);
    ArrayList<String> getTitleKeysForIds(String playlist_id);
    ArrayList<String> getSongsForIds(String playlist_id);
}
