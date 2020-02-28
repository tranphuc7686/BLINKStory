package com.example.blinkstory.elements;

import com.example.blinkstory.model.entity.Element;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ElementsPresenterImpl implements ElementsPresenter {
    ElementsView elementsView;
    ElementsInteractor elementsInteractor;

    public ElementsPresenterImpl(ElementsInteractor elementsInteractor) {
        this.elementsInteractor = elementsInteractor;
    }
    @Override
    public void setDataElements(int idCtg, int index) {

        elementsInteractor.onGetElementApi(idCtg, index).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    elementsView.onGetDataElementRespone((ArrayList<Element>) e);
                }, t -> {
                    elementsView.showError(t.getMessage());
                });

    }

    @Override
    public void setLoadMoreDataElements(int idCtg, int index, OnLoadMoreListenner onLoadMoreListenner) {
        elementsInteractor.onGetElementApi(idCtg, index).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    onLoadMoreListenner.setDataLoadMore((ArrayList<Element>) e);
                }, t -> {
                    onLoadMoreListenner.getMsgErrorLoadMore(t.getMessage());
                });
    }

    @Override
    public void getErrorElement(String mess) {
        elementsView.showError(mess);
    }

    @Override
    public void setView(ElementsView view) {
        this.elementsView = view;
    }

}
