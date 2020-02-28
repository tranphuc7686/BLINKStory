package com.example.blinkstory.ulti;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileHelper {
    public static String getPath(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {

            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");

            final String docId = dat[1];
            final String type = dat[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static Intent redirectActivitySetWallpaper(Uri image) {
        //Start Crop Activity
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //can't use normal URI, because it requires the Uri from file
        intent.setDataAndType(image, "image/*");
        intent.putExtra("mimeType", "image/*");
        return Intent.createChooser(intent, "Set Image");

    }

    public static String getPathFileImageInDevice(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
    }

    public static void saveImageToGallery(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        Toast.makeText(context, "Save at : " + path, Toast.LENGTH_SHORT).show();
    }

    public static void saveVideoToDevice(Context context, File videoFile) {
        MediaPlayer mp = MediaPlayer.create(context, Uri.parse(videoFile.getAbsolutePath()));
        int duration = mp.getDuration();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, "My video title");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        values.put(MediaStore.Video.Media.DURATION, duration);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }


    public static void downloadFile(String url, OnDownloadListener onDownloadListener) {
        new AsyncTask<String, Void, File>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected File doInBackground(String... url) {
                File file = null;
                try {
                    URL u = new URL(url[0]);
                    InputStream is = u.openStream();

                    DataInputStream dis = new DataInputStream(is);

                    byte[] buffer = new byte[1024];
                    int length;
                    file = new File(Environment.getExternalStorageDirectory() + "/" + url[1] + ".mp4");
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((length = dis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    fos.flush();
                    // closing streams
                    fos.close();
                    dis.close();
                } catch (Exception ex) {
                    onDownloadListener.onDownloadError(ex.getMessage());
                }
                return file;

            }

            @Override
            protected void onPostExecute(File file) {

                onDownloadListener.onDownloadSuccess(file);
            }

        }.execute(url);


    }
    public static void uploadFile(String path){

    }
    public static byte[] getThubmailImage(String path) {

        Bitmap thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),
                240, 240);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static byte[] getThubmailVideo(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return b;
    }
    public static byte[] getFileDataContent(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    public interface OnDownloadListener {
        void onDownloadError(String msg);

        void onDownloadSuccess(File file);
    }
    public interface OnUploadFileListener {
        void onUploadError(String msg);

        void onUploadSuccess();
    }
}
