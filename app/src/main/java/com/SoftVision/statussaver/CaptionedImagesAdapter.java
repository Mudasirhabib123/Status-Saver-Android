package com.SoftVision.statussaver;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.util.ArrayList;

public class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {
    Utils utils = new Utils();
    private ArrayList<String> imageIds;
    private Listener listener;
    private Context context;
    private int height;


    interface Listener{
        void onClick(ArrayList<String> files_path, int pos);
        void onItemCopied(File file);
    }
    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cv){
            super(cv);
            cardView = cv;
        }
    }

    public CaptionedImagesAdapter(ArrayList<String> caption, Context context, int height){
        imageIds = caption;
        this.context = context;
        this.height = height;
    }

    public CaptionedImagesAdapter() { }

    @Override
    public int getItemCount(){
        return imageIds.size();
    }


    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        CardView cv = holder.cardView;

        cv.getLayoutParams().height = height;
        Log.e("device width", ",,,,,,,,,,,,,,,,,,,  "+ height);

        ImageButton play_button, download_button, share_button;
        ImageView imageView = (ImageView) cv.findViewById(R.id.info_iamge);
        play_button = (ImageButton) cv.findViewById(R.id.play_button);
        download_button = (ImageButton) cv.findViewById(R.id.download_button);
        share_button = (ImageButton) cv.findViewById(R.id.share_button);

        String current_file = imageIds.get(position);

        File imgFile = new File(current_file);
        Uri uri = Uri.fromFile(imgFile.getAbsoluteFile());

        boolean is_image_file = (current_file.endsWith(".jpg"));

        if (imgFile.exists()) {
            if (! is_image_file) {
                Glide.with(cv).load(uri).into(imageView);
            } else if (is_image_file) {
                play_button.setEnabled(false);
                play_button.setVisibility(View.INVISIBLE);
                Picasso.get().load(uri).fit().into(imageView);
            }
        }

                play_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("CURRENT_FILE", imageIds );
                        intent.putExtra("POSITION", position );
                        context.startActivity(intent);
                    }
                });


                share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (is_image_file) {
                            utils.share_image(context, current_file);
                        }
                        else {
                            utils.shareVideo(imgFile.getName(), current_file, context);
                        }
                    }
                });

                download_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File output_file = utils.copy_file(imgFile);
                        if (listener != null){
                            listener.onItemCopied(output_file);
                        }
                    }
                });

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (is_image_file){
                    intent = new Intent(context, ImageOpener.class);
                }
                else {
                    intent = new Intent(context, VideoPlayer.class);
                }
                intent.putExtra("POSITION", position );
                context.startActivity(intent);
            }
        });
    }
}