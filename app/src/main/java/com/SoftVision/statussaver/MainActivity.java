package com.SoftVision.statussaver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public int permission_request_code = 698;
    Utils utils = new Utils();
    int height;

    @Override
    protected void onStart(){
        super.onStart();

        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, permission_request_code );
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, permission_request_code);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        actionBar.setElevation(0);

        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.whatsapp_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                PackageManager pm = getPackageManager();
                try {
                    intent = pm.getLaunchIntentForPackage("com.whatsapp");
                    if (intent == null){
                        throw new  PackageManager.NameNotFoundException();
                    }
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e){
                    Snackbar.make(getCurrentFocus() ,"Please Install Whatsapp First", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
       }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.share_app:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Whatsapp Status Saver");
                String shareMessage = "\nAmazing App to manage Whatsapp Statuses.\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(shareIntent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class SectionPagerAdapter extends FragmentPagerAdapter{
        public SectionPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return 3;
        }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return new ImagesFragment();
                case 1:
                    return new VideosFragment();
                case 2:
                    return new GalleryFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return getResources().getString(R.string.images_tab);
                case 1:
                    return getResources().getString(R.string.videos_tab);
                case 2:
                    return getResources().getString(R.string.gallery_tab);
            }
            return null;
        }
    }
}

