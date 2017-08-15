package com.prabandham.abhinav.mymusicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Abhinav on 28-Jan-17.
 */
public class PageFragment extends Fragment {

    ImageView imageView;
    TextView textView;
    public static PageFragment create(String art,String name) {
        PageFragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumart", art);
        bundle.putString("albumname",name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.album_image,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageView=(ImageView)view.findViewById(R.id.imageView) ;
        textView=(TextView)view.findViewById(R.id.textView17);
        Bundle arguments=getArguments();
            Picasso.with(getContext()).load(new File(arguments.getString("albumart")+"")).transform(new CustomTransformation(180,180)).placeholder(R.drawable.musicicon1).resize(180,180).onlyScaleDown().into(imageView);
        textView.setText(arguments.getString("albumname")+"");



    }
}
