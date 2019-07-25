package com.example.blinkstory.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.example.blinkstory.model.repository.IElement;
import com.example.blinkstory.model.repository.IElementImpl;
import com.example.blinkstory.view.IUploadView;

public class UploadFilePresenter implements IUploadFilePresenter {
    private IUploadView mIUploadView;
    private ProgressBar mProgressBar;
    private IElement iElement;
    private Context context;


    public UploadFilePresenter(IUploadView mIUploadView, ProgressBar mProgressBar,Context context) {
        this.mIUploadView = mIUploadView;
        this.mProgressBar = mProgressBar;
        this.context = context;
        iElement = new IElementImpl(this);
    }

    public void setOnUploadFileTask(int idCategory,int typeData,String pathFile) {
        iElement.onPushElement(context,idCategory,typeData,pathFile);
    }


    @Override
    public void onUploadFileSuccess(String path) {
        mIUploadView.onSusscess();
    }

    @Override
    public void onUploadFileFailed(String msg) {
        mIUploadView.onFailed(msg);
    }

    @Override
    public void onTurnOffProcessbar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTurnOnProcessbar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
}
