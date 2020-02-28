package com.example.blinkstory.categories;

public interface CategoriesPresenter {
     void getDataListViewCtg();
     void onErrorCtgApi(String statusError);
    void setView(CategoriesView view);
}
