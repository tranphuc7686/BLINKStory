package com.example.blinkstory.elements;

import com.example.blinkstory.model.entity.Element;

import java.util.ArrayList;

public interface OnLoadMoreListenner {
    void setDataLoadMore(ArrayList<Element> elements);
    void getMsgErrorLoadMore(String msg);
}
