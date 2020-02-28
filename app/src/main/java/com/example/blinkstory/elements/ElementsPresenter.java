package com.example.blinkstory.elements;

public interface ElementsPresenter {
    void setDataElements(int idCtg, int index);
    void setLoadMoreDataElements(int idCtg, int index,OnLoadMoreListenner onLoadMoreListenner);
    void getErrorElement(String mess);
    void setView(ElementsView view);

}
