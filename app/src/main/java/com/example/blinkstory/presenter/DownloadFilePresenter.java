package com.example.blinkstory.presenter;

import android.view.View;
import android.widget.ProgressBar;

import com.example.blinkstory.model.repository.DownloadFileAsynTask;
import com.example.blinkstory.model.repository.IDownloadAsynTask;
import com.example.blinkstory.view.IDownloadFileView;

import java.io.File;

public class DownloadFilePresenter implements IDownloadFilePresenter{
    private IDownloadFileView iDownloadFileView;
    private String urlDownload;
    private ProgressBar mProcessProgressBar;
    private IDownloadAsynTask iDownloadAsynTask;
    private String id;
    public DownloadFilePresenter(IDownloadFileView iDownloadFileView,String id,String urlDownload,ProgressBar mProcessProgressBar){
        this.iDownloadFileView = iDownloadFileView;
        this.mProcessProgressBar = mProcessProgressBar;
        this.urlDownload = urlDownload;
        this.id =id;
        iDownloadAsynTask = new DownloadFileAsynTask(this);
    }
    public void setOnDownloadTask(){
        iDownloadAsynTask.onStartDownloadFile(id,urlDownload);
    }

    @Override
    public void onDownloadFileSuccess(File file) {
        iDownloadFileView.onSusscess(file);
    }

    @Override
    public void onDownloadFileFailed(String msg) {
        iDownloadFileView.onFailed(msg);
    }

    @Override
    public void onTurnOffProcessbar() {
        mProcessProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTurnOnProcessbar() {
        mProcessProgressBar.setVisibility(View.VISIBLE);
    }
}
