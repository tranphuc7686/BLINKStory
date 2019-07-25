package com.example.blinkstory.presenter;

import com.example.blinkstory.model.entity.Element;

import java.util.ArrayList;

public interface IElementPresenter {
     void onGetDataElementListener(ArrayList<Element> elements);
     void onGetDataErrorListener(String mess);
     void onVisiableProcessBar();
     void onGoneProcessBar();
    void onAddSuccess();
    void onAddFailed(String msg);
}
