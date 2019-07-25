package com.example.blinkstory.model.repository;

import android.content.Context;

public interface IElement {
    public void onGetElementApi(Context context,int idCategory,int index);
    void onPushElement(Context context,int idCategory,int typeData,String pathFile);


}
