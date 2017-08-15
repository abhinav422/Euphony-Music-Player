package com.prabandham.abhinav.mymusicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Abhinav on 11-Oct-16.
 */

public class FragmentParent extends Fragment{
    CustomPagerAdapter customPagerAdapter;
    ViewPager viewpager;
    PagerTabStrip pagertab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        customPagerAdapter=new CustomPagerAdapter(getChildFragmentManager(),getContext());
        viewpager=(ViewPager)view.findViewById(R.id.viewpager);
        pagertab=(PagerTabStrip)view.findViewById(R.id.pagertab);
        pagertab.setTabIndicatorColorResource(R.color.white);
        pagertab.setTextColor( ContextCompat.getColor(getActivity(), R.color.white));
        viewpager.setAdapter(customPagerAdapter);
       // Toast.makeText(getContext(),"songtab parent view created",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getContext(),"songtab parent resumed",Toast.LENGTH_SHORT).show();
    }
}
