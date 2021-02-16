package com.SoftVision.statussaver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class VideosFragment extends Fragment {
    Utils utils = new Utils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView videosRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_images, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.widthPixels /2;

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(utils.load_videos(), getContext(), height);
        videosRecycler.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        videosRecycler.setLayoutManager(gridLayoutManager);
        videosRecycler.setHasFixedSize(true);
        videosRecycler.setNestedScrollingEnabled(false);
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(ArrayList<String> files_path, int position) {}

            @Override
            public void onItemCopied(File file) {
                Uri uri = Uri.fromFile(file);
                Intent scanFileIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                getActivity().sendBroadcast(scanFileIntent);
                Snackbar.make(getView(), "Video copied", Snackbar.LENGTH_SHORT).show();

            }
        });

        return videosRecycler;
    }
}