package com.example.blinkstory.model.entity;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.repository.ICategory;
import com.example.blinkstory.presenter.IMainPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ICategoryImpl implements ICategory {

    private IMainPresenter iMainPresenter;



    public ICategoryImpl(IMainPresenter iMainPresenter) {
        this.iMainPresenter = iMainPresenter;
    }



    @Override
    public void getCategoryApi(Context context) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);


        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, MainConstant.GET_LIST_CATEGORY_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    iMainPresenter.getDataListViewCtg(parseJsonCtg(response));
                } catch (JSONException e) {
                    iMainPresenter.onErrorCtgApi(e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iMainPresenter.onErrorCtgApi(error.toString());
                    }
                });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    private ArrayList<Category> parseJsonCtg(JSONArray response) throws JSONException {

        ArrayList<Category> resulf = new ArrayList<>();
        //loop through each object
        for (int i = 0; i<response.length(); i++){
            JSONObject jsonProductObject = response.getJSONObject(i);
            int id = jsonProductObject.getInt("id");
            String name = jsonProductObject.getString("name");
            String urlCategory = jsonProductObject.getString("urlCategory");
            resulf.add(new Category(id,name,urlCategory));

        }
        return resulf;
    }

}
