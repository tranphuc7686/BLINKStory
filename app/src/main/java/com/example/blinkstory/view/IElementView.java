package com.example.blinkstory.view;

import com.example.blinkstory.model.entity.Element;

import java.util.ArrayList;

public interface IElementView {
    public void onGetDataElementRespone(ArrayList<Element> elements);
    public void onGetDataElementErrorListener(String mess);

}