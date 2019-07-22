package com.example.blinkstory.presenter;

import com.example.blinkstory.model.entity.Element;

import java.util.ArrayList;

public interface IElementPresenter {
    public void onGetDataElementListener(ArrayList<Element> elements);
    public void onGetDataErrorListener(String mess);
    public void onVisiableProcessBar();
    public void onGoneProcessBar();
}
