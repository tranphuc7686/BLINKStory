package com.example.blinkstory.view;

import java.io.File;

public interface IDownloadFileView {
     void onSusscess(File file);
     void onFailed(String msg);
}
