package com.SoftVision.statussaver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GalleryFragment extends Fragment {
    Utils utils = new Utils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView galleryRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_images, container, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.widthPixels / 2;

        CaptionedGalleryAdapter adapter = new CaptionedGalleryAdapter(utils.load_saved(), getActivity(), height);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        galleryRecycler.setAdapter(adapter);
        galleryRecycler.setLayoutManager(gridLayoutManager);
        galleryRecycler.setHasFixedSize(true);

        return galleryRecycler;
    }
}