package com.example.blinkstory.elements;

import com.example.blinkstory.BaseView;
import com.example.blinkstory.model.entity.Element;

import java.util.ArrayList;

public interface ElementsView extends BaseView {
     void onGetDataElementRespone(ArrayList<Element> elements);
}