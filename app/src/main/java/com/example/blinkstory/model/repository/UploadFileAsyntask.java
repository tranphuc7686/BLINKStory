package com.example.blinkstory.model.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.presenter.IUploadFilePresenter;

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

public class UploadFileAsyntask implements IUploadFileAsyntask {
    private IUploadFilePresenter mIUploadFilePresenter;
    private Context context;

    public UploadFileAsyntask(IUploadFilePresenter mIUploadFilePresenter, Context context) {
        this.mIUploadFilePresenter = mIUploadFilePresenter;
        this.context = context;
    }

    @Override
    public void onUploadFileAsyntask(final String pathFile, int idCtg, final int typeData) {
            if(typeData == MainConstant.TYPE_IMAGE){
                PushImage(pathFile,idCtg);
            }
            else{
                PushVideo(pathFile,idCtg);
            }
    }

    public byte[] getFileDataFromDrawable(String path) {
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

    public byte[] getThubmailVideo(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b  = baos.toByteArray();
        return b;
    }

    private void PushVideo(final String pathFile, final int idCtg) {
        mIUploadFilePresenter.onTurnOnProcessbar();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, MainConstant.URL_UPLOAD_DATA,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            final String urlThumbail = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                            // add content video
                            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, MainConstant.URL_UPLOAD_DATA,
                                    new Response.Listener<NetworkResponse>() {
                                        @Override
                                        public void onResponse(NetworkResponse response) {
                                            try {
                                                String urlImage = new String(response.data,
                                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                                                new IElementImpl().onPushElement(context,idCtg,new Element("","",urlImage,MainConstant.TYPE_VIDEO,urlThumbail));



                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                                return;
                                            }
                                            //  iv_imageview.setImageBitmap(bitmap);
                                            mIUploadFilePresenter.onUploadFileSuccess();
                                            mIUploadFilePresenter.onTurnOffProcessbar();


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @SuppressLint("LongLogTag")
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            mIUploadFilePresenter.onTurnOffProcessbar();
                                            mIUploadFilePresenter.onDownloadFileFailed(error.getMessage());
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
                                    params.put("file", new DataPart(imagename + pathFile.substring(pathFile.lastIndexOf(".")), getFileDataFromDrawable(pathFile)));
                                    return params;
                                }
                            };
                            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                                    999999999,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            //adding the request to volley
                            Volley.newRequestQueue(context).add(volleyMultipartRequest);

                            //

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return;
                        }
                        //  iv_imageview.setImageBitmap(bitmap);
                        mIUploadFilePresenter.onUploadFileSuccess();
                        mIUploadFilePresenter.onTurnOffProcessbar();


                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mIUploadFilePresenter.onTurnOffProcessbar();
                        mIUploadFilePresenter.onDownloadFileFailed(error.getMessage());
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
                params.put("file", new DataPart(imagename + "thumbail.jpg", getThubmailVideo(pathFile)));
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

    private void PushImage(final String pathFile, final int idCtg) {
        mIUploadFilePresenter.onTurnOnProcessbar();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, MainConstant.URL_UPLOAD_DATA,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String urlImage = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                            new IElementImpl().onPushElement(context,idCtg, new Element(null, "", urlImage, MainConstant.TYPE_IMAGE, ""));


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return;
                        }
                        //  iv_imageview.setImageBitmap(bitmap);
                        mIUploadFilePresenter.onUploadFileSuccess();
                        mIUploadFilePresenter.onTurnOffProcessbar();


                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mIUploadFilePresenter.onTurnOffProcessbar();
                        mIUploadFilePresenter.onDownloadFileFailed(error.getMessage());
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
                params.put("file", new DataPart(imagename + pathFile.substring(pathFile.lastIndexOf(".")), getFileDataFromDrawable(pathFile)));
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

}
