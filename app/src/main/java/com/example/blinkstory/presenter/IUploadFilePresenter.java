package com.example.blinkstory.presenter;

public interface IUploadFilePresenter {
    void onUploadFileSuccess();
    void onDownloadFileFailed(String msg);
    void onTurnOffProcessbar();
    void onTurnOnProcessbar();
}
