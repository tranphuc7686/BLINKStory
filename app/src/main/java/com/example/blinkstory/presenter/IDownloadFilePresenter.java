package com.example.blinkstory.presenter;

import java.io.File;

public interface IDownloadFilePresenter {
     void onDownloadFileSuccess(File file);
     void onDownloadFileFailed(String msg);
     void onTurnOffProcessbar();
     void onTurnOnProcessbar();


}
