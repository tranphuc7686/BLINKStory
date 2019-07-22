package com.example.blinkstory.view;

import com.example.blinkstory.model.entity.Category;

import java.util.ArrayList;

public interface IMainView {
    public void onDataResponse(ArrayList<Category> categories);
    public void onSetProgressBarVisibility();
    public void onSetProgressBarGone();
    public void onDataSponseError(String status);
}
