package com.example.blinkstory.presenter;

import android.view.View;
import android.widget.ProgressBar;

import com.example.blinkstory.model.repository.IUploadFileAsyntask;
import com.example.blinkstory.model.repository.UploadFileAsyntask;
import com.example.blinkstory.view.IUploadView;

public class UploadFilePresenter implements IUploadFilePresenter {
    private IUploadView mIUploadView;
    private ProgressBar mProgressBar;
    private IUploadFileAsyntask mIUploadFileAsyntask;

    public UploadFilePresenter(IUploadView mIUploadView, ProgressBar mProgressBar) {
        this.mIUploadView = mIUploadView;
        this.mProgressBar = mProgressBar;
        mIUploadFileAsyntask = new UploadFileAsyntask(this);
    }

    public void setOnUploadFileTask(String pathFile){
        mIUploadFileAsyntask.onUploadFileAsyntask(pathFile);
    }


    @Override
    public void onUploadFileSuccess() {
        mIUploadView.onSusscess();
    }

    @Override
    public void onDownloadFileFailed(String msg) {
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
