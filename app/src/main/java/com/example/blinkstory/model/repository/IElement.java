package com.example.blinkstory.model.repository;

import android.content.Context;

import com.example.blinkstory.model.entity.Element;

public interface IElement {
    public void onGetElementApi(Context context,int idCategory,int index);
    void onPushElement(Context context,int idCategory,Element model);
}
