package com.prabandham.abhinav.mymusicplayer;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Abhinav on 27-Jan-17.
 */
public interface SelectedItem {
    public ArrayList<String> getItemSelected();
    public void setItemSelected(ArrayList<String> item_selected);
    public void setItemSelected(String item_selected);
    public void createPlaylist();
    public Cursor getCursor();
    public void setCursor(Cursor cursor);
}
