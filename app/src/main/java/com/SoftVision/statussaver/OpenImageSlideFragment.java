package com.SoftVision.statussaver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

public class OpenImageSlideFragment extends Fragment {
    Utils utils = new Utils();
    ImageButton save_button, share_button;
    private ArrayList<String> images_path = utils.load_images();;
    private int position;
    String current_file;

    public OpenImageSlideFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_open_image_slide, container, false);
        TouchImageView preview = (TouchImageView) rootView.findViewById(R.id.preview_image);
//        images_path = utils.load_images();
        Log.d("images path", "+++++++++++++++++++++++++++"+images_path.size());
        current_file = images_path.get(position);
        File imgFile = new File(current_file);
        Uri uri = Uri.fromFile(imgFile.getAbsoluteFile());
        Picasso.get().load(uri).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("bitmaploaded", "........................loaded");
                preview.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("bitmapFailed", "........................failed");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                preview.setImageBitmap(myBitmap);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
        });
        //          Glide.with(getContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                preview.setImageBitmap(resource);
//            }
//        });

        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d("onImageFailed", "printstack \n");
                exception.printStackTrace();
            }
        });

        save_button = rootView.findViewById(R.id.save_image);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File output_file = utils.copy_file(imgFile);
                Uri uri = Uri.fromFile(output_file);
                Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                getActivity().sendBroadcast(scanFileIntent);
                Snackbar.make(getView(), "Image copied", Snackbar.LENGTH_SHORT).show();
            }
        });

        share_button = (ImageButton) rootView.findViewById(R.id.share_image);
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.share_image(getActivity(), current_file);
            }
        });
        return rootView;
    }
}