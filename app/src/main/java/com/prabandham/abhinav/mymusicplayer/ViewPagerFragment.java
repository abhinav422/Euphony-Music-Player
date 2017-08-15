package com.prabandham.abhinav.mymusicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vansuita.gaussianblur.GaussianBlur;

import java.io.File;

/**
 * Created by Abhinav on 17-Jun-17.
 */

public class ViewPagerFragment extends Fragment {

    private ImageView imageView;
    private TextView textView;
    private RelativeLayout rl;
    private RelativeLayout rl1;
    private TextView textView1;
    private TextView textView2;
    String album;
    String artist;
    String album_image;
    private BitmapDrawable bd;

    public static ViewPagerFragment getInstance(String album_image, String artist, String album)
    {

        ViewPagerFragment viewPagerFragment=new ViewPagerFragment();
        Bundle bundle=new Bundle();
        bundle.putString("albumimage",album_image);
        bundle.putString("artist",artist);
        bundle.putString("album",album);
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.play_song, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView2);
        textView = (TextView) view.findViewById(R.id.textView);
        rl = (RelativeLayout) view.findViewById(R.id.psrl);
        rl1 = (RelativeLayout) view.findViewById(R.id.rl1);
        textView1 = (TextView) view.findViewById(R.id.textView5);
        textView2 = (TextView) view.findViewById(R.id.textView6);
        Bundle bundle=getArguments();
        album_image=bundle.getString("albumimage");
        album=bundle.getString("album");
        artist=bundle.getString("artist");
        setUpFragment();
    }
    
    private void setUpFragment()
    {

        textView1.setText(album);
        textView2.setText(artist);
        textView1.setSelected(true);
        textView2.setSelected(true);
        Picasso.with(getContext()).load(new File(album_image + "")).resize(200, 200).into(imageView);
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;
        bitmap = BitmapFactory.decodeFile(album_image + "", options);
        if (bitmap != null) {

            bitmap = GaussianBlur.with(getContext()).radius(10).noScaleDown(true).render(bitmap);
            bd = new BitmapDrawable(getResources(), (bitmap));
            rl.setBackground(bd);
        }
        else{
            Picasso.with(getContext()).load(R.drawable.guitar3).resize(200, 200).into(imageView);
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.guitar3, options);
            bitmap = GaussianBlur.with(getContext()).radius(10).noScaleDown(true).render(bitmap);
            bd = new BitmapDrawable(getResources(), (bitmap));
            rl.setBackground(bd);
        }

    }
}
