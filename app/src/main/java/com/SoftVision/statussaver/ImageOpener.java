package com.SoftVision.statussaver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class ImageOpener extends AppCompatActivity {
    Utils utils = new Utils();

    private ArrayList<String> images_paths= utils.load_images();;
    private int size_of_pager, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        images_paths = utils.load_images();
        Log.d("images path", "================================"+images_paths.size());
        position = getIntent().getExtras().getInt("POSITION");
        size_of_pager = images_paths.size();

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(size_of_pager);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter{
        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return size_of_pager;
        }

        @Override
        public Fragment getItem(int pos){
            return new OpenImageSlideFragment(pos);
        }
    }
}
