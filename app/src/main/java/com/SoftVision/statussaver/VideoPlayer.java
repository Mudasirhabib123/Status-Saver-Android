package com.SoftVision.statussaver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity {
    Utils utils = new Utils();
    private ArrayList<String> videos_paths;
    private VideoView videoview;
    String current_file;
    private int size_of_pager, position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        videos_paths = getIntent().getExtras().getStringArrayList("CURRENT_FILE");
        videos_paths = utils.load_videos();
        position = getIntent().getExtras().getInt("POSITION");
        size_of_pager = videos_paths.size();

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(size_of_pager);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return size_of_pager;
        }

        @Override
        public Fragment getItem(int position){
            return new OpenVideosSlideFragment(position);
        }
    }
}