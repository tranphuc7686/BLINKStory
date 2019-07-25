package com.example.blinkstory.presenter;

public interface IUploadFilePresenter {
    void onUploadFileSuccess(String path);
    void onUploadFileFailed(String msg);
    void onTurnOffProcessbar();
    void onTurnOnProcessbar();
}
