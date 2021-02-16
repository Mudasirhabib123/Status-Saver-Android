package com.SoftVision.statussaver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.provider.Settings.System.getString;

public class Utils {

    private String status_path = "WhatsApp/Media/.Statuses/";
    private String saved_content_path = "/My Satauses/Saved Status";

    public ArrayList<String> load_images(){

        File path = new File(Environment.getExternalStoragePublicDirectory(""), status_path);
        ArrayList<String > imagePaths = new ArrayList<>();
        File[] all_images = path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.getPath().endsWith(".jpg"));
            }
        });

        if (all_images != null) {
            for (int i = all_images.length - 1; i >= 0; i--) {
                System.out.println(all_images[i].getAbsolutePath());
                imagePaths.add(all_images[i].getAbsolutePath());
            }
        }
        return  imagePaths;
    }

    public ArrayList<String > load_videos(){
        File path = new File(Environment.getExternalStoragePublicDirectory(""),status_path);
        File[] all_videos = path.listFiles();

        ArrayList<String> videoPaths = new ArrayList<>();

        if (all_videos != null) {
            for (int i = all_videos.length - 1; i >= 0; i--) {
                System.out.println(all_videos[i].getAbsolutePath());
                if (all_videos[i].getAbsolutePath().endsWith(".mp4")) {
                    if (all_videos[i].canExecute()) {
                        videoPaths.add(all_videos[i].getAbsolutePath());
                    }
                }
            }
        }
        return videoPaths;
    }

    public ArrayList<String > load_saved(){
        File path = new File(Environment.getExternalStoragePublicDirectory(""),saved_content_path);
        File[] all_files = path.listFiles();

        ArrayList<String> filesPath = new ArrayList<>();
        if (all_files != null) {
            for (int i = all_files.length - 1; i >= 0; i--) {
                System.out.println(all_files[i].getAbsolutePath());
                if (all_files[i].getAbsolutePath().endsWith(".mp4") || all_files[i].getAbsolutePath().endsWith(".jpg")) {
                    if (all_files[i].isFile()) {
                        filesPath.add(all_files[i].getAbsolutePath());
                    }
                }
            }
        }
        return filesPath;
    }

    public File copy_file(File imgFile){
        String name = imgFile.getName();
        File storagePath = new  File(Environment.getExternalStorageDirectory() + saved_content_path);

        storagePath.mkdirs();
        File output_file = new File(storagePath, name);

        try {
            FileInputStream in = new FileInputStream(imgFile);
            FileOutputStream out = new FileOutputStream(output_file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return output_file;
    }

    public void share_image(Context activity, String path){
        File imgFile = new File(path);
        Bitmap icon = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(path);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        activity.startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void shareVideo(final String title, String path, Context activity) {
        MediaScannerConnection.scanFile(activity, new String[] { path },
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT, title);
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_TITLE, title);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        activity.startActivity(Intent.createChooser(shareIntent, title));
                    }
                }
            );
        }
}
