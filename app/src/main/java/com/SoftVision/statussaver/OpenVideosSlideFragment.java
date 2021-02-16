package com.SoftVision.statussaver;

import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;

public class OpenVideosSlideFragment extends Fragment {
    Utils utils = new Utils();
    VideoPlayer videoPlayer = new VideoPlayer();
    VideoView videoview;
    ImageButton save_button, share_button;

    boolean isVisible = false;
    private int position;

    public OpenVideosSlideFragment(int pos){
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

        ArrayList<String> videoPaths = utils.load_videos();
        String current_video = videoPaths.get(position);
        File videoFile = new File(current_video);
        Uri video = Uri.parse(current_video);

        videoview = (VideoView) rootView.findViewById(R.id.video_player);
        videoview.setMediaController(new MediaController(getActivity()));
        videoview.setVideoURI(video);

        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("media player error ", "" + what + "   "+ extra);
                return true;
            }
        });

        save_button = (ImageButton) rootView.findViewById(R.id.save_video);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File copy = new File(current_video);
                File output_file = utils.copy_file(copy);
                Uri uri = Uri.fromFile(output_file);
                Intent scanFileIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                getActivity().sendBroadcast(scanFileIntent);
                Snackbar.make(getView(), "Video copied", Snackbar.LENGTH_SHORT).show();
            }
        });

        share_button = (ImageButton) rootView.findViewById(R.id.share_video);
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File copy = new File(current_video);

                utils.shareVideo(copy.getName(), current_video, getActivity());
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
                videoview.pause();
                viewPager.setCurrentItem(cur_position);
                play_video(videoview, videoPaths);
            }
        });

        play_video(videoview, videoPaths);
        return rootView;
    }

    public void play_video(VideoView videoview, ArrayList<String> videoPaths){
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


}