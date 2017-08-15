package com.prabandham.abhinav.mymusicplayer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Abhinav on 28-Jan-17.
 */
public class ImagePagerAdapter extends PagerAdapter {
    ArrayList<String> album_art;
    Context context;
    public ImagePagerAdapter(Context context, ArrayList<String> x)
    {
        this.context=context;
        this.album_art=x;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context,R.layout.album_image2,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.imageView);

        Picasso.with(context).load(new File(album_art.get(position)+"")).placeholder(R.drawable.musicicon).resize(imageView.getWidth(),imageView.getHeight()).into(imageView);
        container.addView(view);
        return view;

    }


    @Override
    public int getCount() {
        return album_art.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
