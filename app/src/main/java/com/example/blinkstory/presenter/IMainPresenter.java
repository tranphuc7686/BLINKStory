package com.example.blinkstory.presenter;

import com.example.blinkstory.model.entity.Category;

import java.util.ArrayList;

public interface IMainPresenter {
    public void getDataListViewCtg(ArrayList<Category> categories);
    public void onErrorCtgApi(String statusError);
}
