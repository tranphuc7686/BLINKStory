package com.example.blinkstory.model.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.presenter.IElementPresenter;
import com.example.blinkstory.presenter.IUploadFilePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class IElementImpl implements IElement {
    private IElementPresenter iElementPresenter;
    private IUploadFilePresenter iUploadFilePresenter;

    public IElementImpl(IElementPresenter iElementPresenter) {
        this.iElementPresenter = iElementPresenter;
    }
    public IElementImpl(IUploadFilePresenter iUploadFilePresenter) {
        this.iUploadFilePresenter = iUploadFilePresenter;
    }



    @Override
    public void onGetElementApi(Context context, int idCategory, int index) {
        iElementPresenter.onVisiableProcessBar();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);


        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST,
                MainConstant.GET_LIST_ELEMENT_URL + idCategory + "/" + index,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        iElementPresenter.onGoneProcessBar();
                        try {
                            iElementPresenter.onGetDataElementListener(parseJsonElement(response));
                        } catch (JSONException e) {
                            iElementPresenter.onGetDataErrorListener(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iElementPresenter.onGoneProcessBar();
                        iElementPresenter.onGetDataErrorListener(error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onPushElement(final Context context, final int idCategory, int typeData, final String pathFile) {
        iUploadFilePresenter.onTurnOnProcessbar();
        //check image or video
        if (typeData == MainConstant.TYPE_IMAGE) {
            // upload thumbail
            new UploadFileAsyntask(context, new IUploadFileAsyntask() {
                @Override
                public void onUpdateFileSuccess(final String pathThumbail) {
                    // upload content
                    new UploadFileAsyntask(context, new IUploadFileAsyntask() {
                        @Override
                        public void onUpdateFileSuccess(String pathFileResuf) {
                            onAddElement(context,
                                    idCategory,
                                    new Element("", "", pathFileResuf, MainConstant.TYPE_IMAGE, pathThumbail),
                                    new IAddElement() {
                                        @Override
                                        public void onAddElementSucess() {
                                            iUploadFilePresenter.onUploadFileSuccess("Success");
                                            iUploadFilePresenter.onTurnOffProcessbar();
                                        }

                                        @Override
                                        public void onAddElementFailed(String msg) {
                                            iUploadFilePresenter.onUploadFileFailed(msg);
                                            iUploadFilePresenter.onTurnOffProcessbar();

                                        }
                                    }
                            );
                        }

                        @Override
                        public void onUpdateFileFailed(String msg) {
                            iUploadFilePresenter.onUploadFileFailed(msg);
                            iUploadFilePresenter.onTurnOffProcessbar();
                        }
                    }).startUploadFile(UploadFileAsyntask.getFileDataContent(pathFile), pathFile.substring(pathFile.lastIndexOf(".")));
                }

                @Override
                public void onUpdateFileFailed(String msg) {
                    iUploadFilePresenter.onUploadFileFailed(msg);
                    iUploadFilePresenter.onTurnOffProcessbar();
                }
            }).startUploadFile(UploadFileAsyntask.getThubmailImage(pathFile),"thumnail"+ pathFile.substring(pathFile.lastIndexOf(".")));
        } else {
            // upload thumbail video
            new UploadFileAsyntask(context, new IUploadFileAsyntask() {
                @Override
                public void onUpdateFileSuccess(final String pathThumbail) {
                    // upload content
                    new UploadFileAsyntask(context, new IUploadFileAsyntask() {
                        @Override
                        public void onUpdateFileSuccess(String pathFileResuf) {
                            onAddElement(context,
                                    idCategory,
                                    new Element("", "", pathFileResuf, MainConstant.TYPE_VIDEO, pathThumbail),
                                    new IAddElement() {
                                        @Override
                                        public void onAddElementSucess() {
                                            iUploadFilePresenter.onUploadFileSuccess("Success");
                                            iUploadFilePresenter.onTurnOffProcessbar();
                                        }

                                        @Override
                                        public void onAddElementFailed(String msg) {
                                            iUploadFilePresenter.onUploadFileFailed(msg);
                                            iUploadFilePresenter.onTurnOffProcessbar();
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onUpdateFileFailed(String msg) {
                            iUploadFilePresenter.onUploadFileFailed(msg);
                            iUploadFilePresenter.onTurnOffProcessbar();
                        }
                    }).startUploadFile(UploadFileAsyntask.getFileDataContent(pathFile), pathFile.substring(pathFile.lastIndexOf(".")));
                }

                @Override
                public void onUpdateFileFailed(String msg) {
                    iUploadFilePresenter.onUploadFileFailed(msg);
                    iUploadFilePresenter.onTurnOffProcessbar();
                }
            }).startUploadFile(UploadFileAsyntask.getThubmailVideo(pathFile), "thumnail.jpg");
        }

    }


    public void onAddElement(Context context, final int idCtg, final Element model, final IAddElement iAddElement) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        HashMap<String, String> map = new HashMap<String, String>();
        // pass your input text
        map.put("Id", model.getId());
        map.put("Caption", model.getCaption());
        map.put("Url", model.getUrl());
        map.put("TypeData", model.getTypeData() + "");
        map.put("SrcThumbail", model.getSrcThumbail());
        map.put("IsUser", 2 + "");
        map.put("CategoryId", idCtg + "");
        Log.e("para", map + "");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                MainConstant.URL_ADD_DATA, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        iAddElement.onAddElementSucess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        iAddElement.onAddElementFailed("Error: " + error.getMessage());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);

                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }


        };
        jsonObjReq.setTag(TAG);
// Adding request to request queue
        requestQueue.add(jsonObjReq);

// Cancelling request
/* if (queue!= null) {
queue.cancelAll(TAG);
} */


    }


    private ArrayList<Element> parseJsonElement(JSONArray response) throws JSONException {

        ArrayList<Element> resulf = new ArrayList<>();
        //loop through each object
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonProductObject = response.getJSONObject(i);
            String id = jsonProductObject.getString("id");
            String caption = jsonProductObject.getString("caption");
            String url = jsonProductObject.getString("url");
            int typeData = jsonProductObject.getInt("typeData");
            String srcThumbail = jsonProductObject.getString("srcThumbail");
            int isUser = jsonProductObject.getInt("isUser");
            resulf.add(new Element(id, caption, url, typeData, srcThumbail));

        }
        return resulf;
    }
}
