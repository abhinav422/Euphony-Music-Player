package com.prabandham.abhinav.mymusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Abhinav on 04-Jan-17.
 */
public class SAAGP  implements Songs,Albums,Artists,Genres,Playlists {

    private Cursor cursor;
    private ArrayList<String> arrayList=new ArrayList<>();
    private int column;
    private Context context;
    private String thDuration="5000";
    static SAAGP saagp=null;
    public static SAAGP getInstance(Context context)
    {
        if(saagp==null)
        {
            saagp=new SAAGP(context);
            return saagp;
        }
        return saagp;
    }
    public String getThDuration() {
        return thDuration;
    }

    public void setThDuration(String thDuration) {
        this.thDuration = thDuration;
    }

    @Override
    public String getSortOrder() {
        return sortOrder;
    }

    @Override
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }



    public String getSortOrderForAlbum() {
        return sortOrderForAlbum;
    }

    public void setSortOrderForAlbum(String sortOrderForAlbum) {
        this.sortOrderForAlbum = sortOrderForAlbum;
    }

    public String getSortOrderForArtist() {
        return sortOrderForArtist;
    }

    public void setSortOrderForArtist(String sortOrderForArtist) {
        this.sortOrderForArtist = sortOrderForArtist;
    }

    public String getSortOrderForGenre() {
        return sortOrderForGenre;
    }

    public void setSortOrderForGenre(String sortOrderForGenre) {
        this.sortOrderForGenre = sortOrderForGenre;
    }

    String sortOrder=Songs.sortOrder;
    String sortOrderForAlbum=Albums.sortOrder;
    String sortOrderForArtist=Artists.sortOrder;
    String sortOrderForGenre=Genres.sortOrder;
    //ArrayList<String> title_key=new ArrayList<>();
    public SAAGP()
    {

    }
    public SAAGP(Context context)
    {
        this.context=context;
    }

    public void filter( Uri uri, String [] projection, String selection, String [] selectionArgs, String sortOrder, String column)
    {
        if(arrayList!=null)
            arrayList=new ArrayList<>();

        cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs,sortOrder);
        this.column=cursor.getColumnIndex(column);
        if(cursor!=null && cursor.moveToFirst())
        {
            do{
                if(cursor.getString(this.column)!=null) {
                    arrayList.add(cursor.getString(this.column));
                }
                else
                {
                    arrayList.add(null);
                }

            }while(cursor.moveToNext());
            cursor.close();
        }
        else
            cursor.close();
    }

    public ArrayList<String> getArrayList()
    {
        return arrayList;
    }

    @Override
    public ArrayList<String> getTitlekeyForAlbumAndArtist(String album_key, String artist_id) {
        filter(Songs.uri,Songs.projection,MediaStore.Audio.Media.ALBUM_KEY+"=?"+" AND "+MediaStore.Audio.Media.ARTIST_KEY+"=?"+" AND "+MediaStore.Audio.Media.DURATION + " >= "+thDuration,new String[]{album_key,artist_id},sortOrder,MediaStore.Audio.Media._ID);
        return getArrayList();
    }

    void filterForSongs(String selection,String[] selectionargs,String column)
    {
        filter(Songs.uri,Songs.projection,selection,selectionargs,sortOrder,column);
    }

    @Override
    public ArrayList<String> getTitleKeys() {
        filterForSongs(MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media._ID);
        return getArrayList();
    }



    @Override
    public ArrayList<String> getSongs()
    {
        filterForSongs(MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media.TITLE);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getSongsForTitleKeys(ArrayList<String> title_key) {
        int x=title_key.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filterForSongs(MediaStore.Audio.Media._ID+" IN("+s+")",title_key.toArray(new String[]{}),MediaStore.Audio.Media.TITLE);
        return  getArrayList();
    }

    @Override
    public ArrayList<String> getTitleKeysForSongs(ArrayList<String> songs) {
        int x=songs.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filterForSongs(MediaStore.Audio.Media.TITLE_KEY+" IN("+s+")",songs.toArray(new String[]{}),MediaStore.Audio.Media._ID);
        return getArrayList();
    }

    @Override
    public String getPath(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},MediaStore.Audio.Media.DATA);
        return getArrayList().get(0);
    }

    @Override
    public String getAlbumKeyForSong(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},MediaStore.Audio.Media.ALBUM_KEY);
        return getArrayList().get(0);
    }

    @Override
    public String getAlbum(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},MediaStore.Audio.Media.ALBUM);
        return getArrayList().get(0);
    }

    @Override
    public String getDuration(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},MediaStore.Audio.Media.DURATION);
        if(getArrayList().size()>0)
        return getArrayList().get(0);
        else
            return "0";
    }

    @Override
    public String getArtistKey(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},MediaStore.Audio.Media.ARTIST_KEY);
        return getArrayList().get(0);
    }

    @Override
    public String getArtistForSong(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},MediaStore.Audio.Media.ARTIST);
        return getArrayList().get(0);
    }

    @Override
    public String getBookmark(String x) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{x},Songs.projection[12]);
        return getArrayList().get(0);
    }

    @Override
    public String getTitle(String x) {
        filter(Songs.uri,Songs.projection,MediaStore.Audio.Media._ID+"=?",new String[]{x},Songs.sortOrder,MediaStore.Audio.Media.TITLE);
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getDurationForTitleKeys(ArrayList<String> title_key) {
        int x=title_key.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filterForSongs(MediaStore.Audio.Media._ID+" IN("+s+")",title_key.toArray(new String[]{}),MediaStore.Audio.Media.DURATION);
        return  getArrayList();
    }

    @Override
    public ArrayList<String> getArtistsForTitleKeys(ArrayList<String> title_key) {
        int x=title_key.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filterForSongs(MediaStore.Audio.Media._ID+" IN("+s+")",title_key.toArray(new String[]{}),MediaStore.Audio.Media.ARTIST);
        return  getArrayList();
    }

    @Override
    public String getAlbumKeyForSongAndArtist(String x,String artist) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?"+" AND "+MediaStore.Audio.Media.ARTIST+"=?",new String[]{x,artist},MediaStore.Audio.Media.ALBUM_KEY );
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getSongIds() {
        filterForSongs(MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media._ID);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getDurations() {
        filterForSongs(MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media.DURATION);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getAllArtists() {
        filterForSongs(MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media.ARTIST);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getSongForAlbum(String x) {
        filterForSongs(MediaStore.Audio.Media.ALBUM_KEY+"=?" + " AND "+MediaStore.Audio.Media.DURATION + " >= "+thDuration,new String[]{x},MediaStore.Audio.Media._ID);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getAlbumsForGenre(String id) {
        filter(MediaStore.Audio.Genres.Members.getContentUri("external",Long.parseLong(id)),new String[]{MediaStore.Audio.Genres.Members.ALBUM_KEY},null,null, sortOrderForAlbum,MediaStore.Audio.Media.ALBUM_KEY);
        LinkedHashSet<String> lhs=new LinkedHashSet<>(getArrayList());
        getArrayList().clear();
        getArrayList().addAll(lhs);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getSongidForPlaylist(String id) {
        if(id.equals("Recently Added"))
        {
            filter(Songs.uri,Songs.projection,MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media.DATE_ADDED+ " DESC",MediaStore.Audio.Media.TITLE_KEY);
            return getArrayList();
        }
        else if(id.equals("Last Played"))
        {
            return null;
        }
        else {
            filter(MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(id)), Playlists.membersProjection, MediaStore.Audio.Playlists.Members.DURATION+ " >= "+thDuration, null,MediaStore.Audio.Playlists.Members.PLAY_ORDER, MediaStore.Audio.Playlists.Members._ID);

            return getArrayList();
        }

    }

    @Override
    public ArrayList<String> getIdForPlaylistSong(ArrayList<String> song_id,String playlist_id) {
        int x=song_id.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filter(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(playlist_id)),Playlists.membersProjection,MediaStore.Audio.Playlists.Members.TITLE_KEY+" IN"+"("+s+")",song_id.toArray(new String[]{}),MediaStore.Audio.Playlists.Members.PLAY_ORDER,MediaStore.Audio.Media._ID);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getTitleKeysForIds( String playlist_id) {

        if(playlist_id.equals("Recently Added"))
        {
            filter(Songs.uri,Songs.projection,MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media.DATE_ADDED+ " DESC",MediaStore.Audio.Media._ID);
            return getArrayList();
        }
        filter(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(playlist_id)),Playlists.membersProjection,MediaStore.Audio.Playlists.Members.DURATION+ " >= "+thDuration,null,MediaStore.Audio.Playlists.Members.PLAY_ORDER,MediaStore.Audio.Playlists.Members.AUDIO_ID);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getSongsForIds(String playlist_id) {

        if(playlist_id.equals("Recently Added"))
        {
            filter(Songs.uri,Songs.projection,MediaStore.Audio.Media.DURATION + " >= "+thDuration,null,MediaStore.Audio.Media.DATE_ADDED+ " DESC",MediaStore.Audio.Media.TITLE);
            return getArrayList();
        }
       // song_id.add(0,playlist_id);
        filter(MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(playlist_id)),Playlists.membersProjection,MediaStore.Audio.Playlists.Members.DURATION+ " >= "+thDuration,null,MediaStore.Audio.Playlists.Members.PLAY_ORDER,MediaStore.Audio.Playlists.Members.TITLE);
        return getArrayList();
    }

    @Override
    public String getSongId(String title_key) {
        filterForSongs(MediaStore.Audio.Media._ID+"=?",new String[]{title_key},MediaStore.Audio.Media._ID);
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getAlbums() {
       filterForAlbums(null,null,MediaStore.Audio.Albums.ALBUM_KEY);
        return getArrayList();
    }

    @Override
    public String getAlbumById(String album_id) {
        filterForAlbums(MediaStore.Audio.Albums.ALBUM_KEY+"=?",new String[]{album_id},MediaStore.Audio.Albums.ALBUM);
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getAlbumNameForAlbumKeys(ArrayList<String> album_keys) {
        int x=album_keys.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filterForAlbums(MediaStore.Audio.Albums.ALBUM_KEY+" IN("+s+")",album_keys.toArray(new String[]{}),MediaStore.Audio.Albums.ALBUM);
        return  getArrayList();
    }

    @Override
    public ArrayList<String> getAlbumArt() {
        filterForAlbums(null,null,MediaStore.Audio.Albums.ALBUM_ART);
        return getArrayList();
    }

    public void filterForAlbums(String selection,String[] selectionargs,String columns)
    {
        filter(Albums.uri,Albums.projection,selection,selectionargs,sortOrderForAlbum,columns);
    }

    @Override
    public  String getAlbumArt(String x) {
        filterForAlbums(MediaStore.Audio.Albums.ALBUM_KEY+"=?",new String[]{x},MediaStore.Audio.Albums.ALBUM_ART);
        if(getArrayList().size()!=0)
        return getArrayList().get(0);
        else
            return null;
    }

    @Override
    public String getNoOfSongs(String x) {
        filterForAlbums(MediaStore.Audio.Albums.ALBUM_KEY+"=?",new String[]{x},MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        return  getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getAlbumNames() {
        filterForAlbums(null,null,MediaStore.Audio.Albums.ALBUM);
        return getArrayList();
    }

    @Override
    public String getArtist(String artistId) {
        /*filter(MediaStore.Audio.Artists.Albums.getContentUri("external",Long.parseLong(artistId)),Albums.projection,null,null,sortOrderForAlbum,MediaStore.Audio.Albums.ARTIST);
        return getArrayList().get(0);*/
        filter(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,Artists.projection,MediaStore.Audio.Artists.ARTIST_KEY+"=?",new String[]{artistId},MediaStore.Audio.Artists.DEFAULT_SORT_ORDER,MediaStore.Audio.Artists.ARTIST);
        return getArrayList().get(0);
    }

    @Override
    public String getArtistKeyById(String artistId) {
        filter(Artists.uri,Artists.projection,MediaStore.Audio.Artists.ARTIST_KEY+"=?",new String[]{artistId},sortOrderForArtist,MediaStore.Audio.Artists.ARTIST);
        return getArrayList().get(0);
    }

    @Override
    public String getNoOfSongsForArtist(String artistId) {
        filter(MediaStore.Audio.Artists.Albums.getContentUri("external",Long.parseLong(artistId)),Albums.projection,null,null,sortOrderForAlbum,Albums.projection[8]);
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getArtists() {
        filter(Artists.uri,Artists.projection,null,null,sortOrderForArtist,MediaStore.Audio.Artists.ARTIST);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getArtistKeys() {
        filter(Artists.uri,Artists.projection,null,null,sortOrderForArtist,MediaStore.Audio.Artists.ARTIST_KEY);
        return getArrayList();
    }

    @Override
    public String getArtistId(String x) {
        filter(Artists.uri,Artists.projection,MediaStore.Audio.Artists.ARTIST_KEY+"=?",new String[]{x},sortOrderForArtist,MediaStore.Audio.Artists._ID);
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getGenres()
    {
        String query = " _id in (select genre_id from audio_genres_map where audio_id in (select _id from audio_meta where is_music != 0))" ;

        filter(Genres.uri,Genres.projection,query,null,sortOrderForGenre,MediaStore.Audio.Genres.NAME);
        return getArrayList();
    }

    @Override
    public String getGenreId(String x)
    {
        filter(Genres.uri,Genres.projection,MediaStore.Audio.Genres.NAME+"=?",new String[]{x},sortOrderForGenre,MediaStore.Audio.Genres._ID);
        return getArrayList().get(0);
    }



    @Override
    public String getCount(String x)
    {
        filter(Artists.uri,Artists.projection,Artists.projection[2]+"=?",new String[]{x},sortOrderForArtist,Artists.projection[2]);
        return getArrayList().get(0);
    }

    @Override
    public String getNoOfAlbums(String x)
    {
        filter(Artists.uri,Artists.projection,Artists.projection[2]+"=?",new String[]{x},sortOrderForArtist,Artists.projection[3]);
        return getArrayList().get(0);
    }

    @Override
    public ArrayList<String> getAlbumKeysForArtist(String id)
    {
       filter(MediaStore.Audio.Artists.Albums.getContentUri("external",Long.parseLong(id)),Albums.projection,null,null,sortOrderForAlbum,MediaStore.Audio.Albums.ALBUM_KEY);
        return getArrayList();
    }

    @Override
    public ArrayList<String> getAlbumArtForKeys(ArrayList<String> album_key) {
        int x=album_key.size();
        String s="";
        for(int i=0;i<x-1;i++)
        {

            s=s+"?,";

        }
        s=s+"?";
        filterForAlbums(MediaStore.Audio.Albums.ALBUM_KEY+" IN("+s+")",album_key.toArray(new String[]{}),MediaStore.Audio.Albums.ALBUM_ART);
        return  getArrayList();
    }

    @Override
    public ArrayList<String> getName() {
        filter(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,Playlists.projection,null,null,MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER,MediaStore.Audio.Playlists.NAME);
        return getArrayList();
    }

    @Override
    public String getNameForId(String playlist_id) {
        filter(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,Playlists.projection,MediaStore.Audio.Playlists._ID+"=?",new String[]{playlist_id},null,MediaStore.Audio.Playlists.NAME);
        return getArrayList().get(0);
    }

    @Override
    public String getId(String playlist) {
        filter(Playlists.uri,Playlists.projection,MediaStore.Audio.Playlists.NAME+"=?",new String[]{playlist},Playlists.sortOrder,MediaStore.Audio.Playlists._ID);
        return getArrayList().get(0);
    }
}
