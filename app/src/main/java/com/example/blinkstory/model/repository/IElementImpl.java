package com.example.blinkstory.model.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.presenter.IElementPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IElementImpl implements IElement {
    private IElementPresenter iElementPresenter;

    public IElementImpl(IElementPresenter iElementPresenter) {
        this.iElementPresenter = iElementPresenter;
    }

    public IElementImpl() {

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
    public void onPushElement(Context context, final int idCtg, final Element model) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainConstant.URL_ADD_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<String, String>();
                // pass your input text
                map.put("Id", model.getId());
                map.put("Caption", model.getCaption());
                map.put("Url", model.getUrl());
                map.put("TypeData", model.getTypeData() + "");
                map.put("SrcThumbail", model.getSrcThumbail());
                map.put("IsUser", 2+"");
                map.put("CategoryId",idCtg+"");
                Log.e("para", map + "");
                return map;
            }

        };
        requestQueue.add(stringRequest);


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
