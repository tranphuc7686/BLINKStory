package com.example.blinkstory.details.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.blinkstory.Constant;
import com.example.blinkstory.ulti.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UploadFileAsyntask {

    private Context context;
    public UploadFileAsyntask() {
    }


    public void startUploadFile(final byte[] dataFile, final String fileExtension, final Callback callback) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.URL_UPLOAD_DATA,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String urlImage = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            //  iv_imageview.setImageBitmap(bitmap);
                            callback.onSuccess(urlImage);


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        callback.onFailed("Error: " + error.getMessage());
                        Log.e("VolleyonErrorResponse200", "Error: " + error.getMessage());
                        // Toasty.error(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }

                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + fileExtension, dataFile));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                999999999,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
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

    public interface Callback {
        void onFailed(String msg);
        void onSuccess(String url);
    }


}
