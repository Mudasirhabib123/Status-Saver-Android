package com.SoftVision.statussaver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

public class OpenGallerySlideFragment extends Fragment {
    Utils utils = new Utils();
    VideoPlayer videoPlayer = new VideoPlayer();
    VideoView videoview;
    TouchImageView preview;
    ImageButton vid_save_button, vid_share_button;
    ImageButton img_save_button, img_share_button;
    boolean isImageFile;

    boolean isVisible = false;
    private int position;

    public OpenGallerySlideFragment(int pos){
        position = pos;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setAllowEnterTransitionOverlap(isVisibleToUser);
        Log.d("set user hint visible", "im callsed");
        if (isVisibleToUser){
            isVisible = isVisibleToUser;
        }
        else {
            isVisible = false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_captioned_videos_adapter, container, false);
        ViewGroup imgView = (ViewGroup) inflater.inflate(R.layout.fragment_open_image_slide, container, false);

        ArrayList<String> filesPaths = utils.load_saved();
        String current_file_path = filesPaths.get(position);
        File file = new File(current_file_path);
        Uri uri = Uri.fromFile(file.getAbsoluteFile());
        Log.e(this.toString(),"Position =================  " + position + "== " + current_file_path);

        isImageFile = current_file_path.endsWith(".jpg");
        preview = (TouchImageView) imgView.findViewById(R.id.preview_image);
        img_save_button = (ImageButton) imgView.findViewById(R.id.save_image);
        img_share_button = (ImageButton) imgView.findViewById(R.id.share_image);

        videoview = (VideoView) rootView.findViewById(R.id.video_player);
        vid_save_button = (ImageButton) rootView.findViewById(R.id.save_video);
        vid_share_button = (ImageButton) rootView.findViewById(R.id.share_video);

        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("mediaplayer error", "  gallery" + what + extra);
                return true;
            }
        });

        if (! isImageFile){
            videoview.setMediaController(new MediaController(getActivity()));
            videoview.setVideoURI(uri);


        }

        vid_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File output_file = utils.copy_file(file);
                Uri uri = Uri.fromFile(output_file);
                Intent scanFileIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                getActivity().sendBroadcast(scanFileIntent);
                Snackbar.make(getView(), "Video copied", Snackbar.LENGTH_SHORT).show();
            }
        });
        img_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File output_file = utils.copy_file(file);
                Uri uri = Uri.fromFile(output_file);
                Intent scanFileIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                getActivity().sendBroadcast(scanFileIntent);
                Snackbar.make(getView(), "Image copied", Snackbar.LENGTH_SHORT).show();
            }
        });

        vid_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.shareVideo(file.getName(), current_file_path, getActivity());
            }
        });
        img_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.share_image(getContext(), current_file_path);
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
                Log.d("scroll state changed", "********im called" + state);
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("page scrolled", "********im called");

            }
            public void onPageSelected(int cur_position) {
                Log.d("page sellected", "********im called" + cur_position);
                position = cur_position;
                if (! isImageFile){
                    videoview.pause();
                }
                Log.e(this.toString(),"Position =================  " + position + "and cur_position == " + cur_position);

                isImageFile = filesPaths.get(cur_position).endsWith(".jpg");
                viewPager.setCurrentItem(cur_position);
                if (isImageFile){
//                    setImage(uri,file, preview);
                }
                else {
                    play_video(videoview, filesPaths);
                }
            }
        });

        if (isImageFile){
            setImage(uri,file, preview);
            Log.d("image file", "qqqqqqqqqqqqQQQQQQQQQQQQ");
            return imgView;
        }
        else {
            play_video(videoview, filesPaths);
            Log.d("video file", "qqqqqqqqqqqqQQQQQQQQQQQQVVVVVVVVVV");
            return rootView;
        }
    }

    public void play_video(VideoView videoview, ArrayList<String> videoPaths){
        Log.e("play video","Position =================  " + position);

        Uri video = Uri.parse(videoPaths.get(position));
        videoview.setVideoURI(video);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("on prepared", "im called  " + isVisible);
                if (isVisible) {
                    videoview.start();
                } else {
                    videoview.pause();
                }
            }
        });
    }

    public void pause_video(ViewGroup rootView){
        videoview = (VideoView) rootView.findViewById(R.id.video_player);
        videoview.setMediaController(new MediaController(getActivity()));
        videoview.pause();
    }

    public void setImage(Uri uri,File file, TouchImageView preview){
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        preview.setImageBitmap(bitmap);
//        Picasso.get().load(uri).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Log.d("bitmaploaded", "........................loaded");
//                preview.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                Log.d("bitmapFailed", "........................failed");
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        });
    }


}