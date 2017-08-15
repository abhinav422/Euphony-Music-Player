package com.prabandham.abhinav.mymusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.WindowManager;

/**
 * Created by Abhinav on 25-Apr-17.
 */
public class SettingsFragment extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //MainActivity mainActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setBackgroundDrawableResource(R.drawable.blurred10);
        //getView().setBackgroundResource(R.drawable.blurred3);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        String x=Integer.toString(getIntent().getIntExtra("background",0));
       SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("pref_list",x).apply();
        setActivityBackground(Integer.valueOf(x));
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_check",false))
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_check",true)){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Intent intent =getIntent();
        if(key.equals("pref_check"))
        {
            if(sharedPreferences.getBoolean("pref_check",false)) {


                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }
            else{

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
            intent.putExtra("fullscreen",1);
        }
        else if(key.equals("pref_edittext"))
        {
            if(sharedPreferences.getString("pref_edittext","5000")!=null && !sharedPreferences.getString("pref_edittext","5000").equals("")) {
                if (Integer.parseInt(sharedPreferences.getString("pref_edittext", "5000")) >= 5000) {

                    // intent.putExtra("edittext",sharedPreferences.getString("pref_edittext","5000"));
                    intent.putExtra("edittext", 1);

                }

            }
        }
        else if(key.equals("pref_list"))
        {
             String selection=sharedPreferences.getString("pref_list","1");
            String background[]=getResources().getStringArray(R.array.background_values);

            setActivityBackground(Integer.parseInt(selection));

            intent.putExtra("listpref",1);
        }
        setResult(RESULT_OK,intent);
    }
    private void setActivityBackground(int n) {
        BitmapDrawable bitmapDrawable;
        if( n==0)
        {

            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred9,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);

        }
        else if(n==1)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred10,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
        else if(n==2)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred2,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
        else if(n==3)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred3,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
        else if(n==4)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred8,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }

}


