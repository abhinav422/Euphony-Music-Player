package com.prabandham.abhinav.mymusicplayer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Abhinav on 11-Jan-17.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context fragment) {
        super(fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_image);
    }
}
