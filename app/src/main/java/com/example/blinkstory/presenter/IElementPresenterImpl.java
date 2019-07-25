package com.example.blinkstory.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.model.repository.IElement;
import com.example.blinkstory.model.repository.IElementImpl;
import com.example.blinkstory.view.IElementView;

import java.util.ArrayList;

public class IElementPresenterImpl implements IElementPresenter {
    IElementView iElementView;
    IElement iElement;
    Context mContext;
    ProgressBar progressBar;
    public IElementPresenterImpl(IElementView iElementView, Context mContext, ProgressBar progressBar){
        this.iElementView = iElementView;
        iElement = new IElementImpl(this);
        this.mContext = mContext;
        this.progressBar =progressBar;
    }
    public void setLoadDataElement(int idCtg,int index){
        iElement.onGetElementApi(mContext,idCtg,index);
    }
    @Override
    public void onGetDataElementListener(ArrayList<Element> elements) {
        iElementView.onGetDataElementRespone(elements);
    }

    @Override
    public void onGetDataErrorListener(String mess) {
        iElementView.onGetDataElementErrorListener(mess);
    }

    @Override
    public void onVisiableProcessBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGoneProcessBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAddSuccess() {

    }

    @Override
    public void onAddFailed(String msg) {

    }
}
