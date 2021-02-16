package com.SoftVision.statussaver;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class ImagesFragment extends Fragment {
    Utils utils = new Utils();
    public ArrayList<String> ImagesPaths = utils.load_images();
    int height;

    CaptionedImagesAdapter adapter;
    @Override
    public void onStart(){
        super.onStart();
        Log.e("onStart", "=======================on start called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("onResume", "=====================on resume called");
        ImagesPaths = utils.load_images();
        Log.e("onResume", "size=====================" + ImagesPaths.size());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView imagesRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_images, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.widthPixels / 2;

        adapter = new CaptionedImagesAdapter(ImagesPaths, getActivity(), height);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getActivity().getResources().getInteger(R.integer.layout_param_numbers));
        imagesRecycler.setAdapter(adapter);
        imagesRecycler.setLayoutManager(gridLayoutManager);
        imagesRecycler.setHasFixedSize(true);




        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(ArrayList<String> files_path, int position) {
            }

            @Override
            public void onItemCopied(File file) {
                Uri uri = Uri.fromFile(file);
                Intent scanFileIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                getActivity().sendBroadcast(scanFileIntent);
                Snackbar.make(getView(), "Image copied", Snackbar.LENGTH_SHORT).show();
            }
        });
        return imagesRecycler;
    }

}