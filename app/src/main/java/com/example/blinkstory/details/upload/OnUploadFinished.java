package com.example.blinkstory.details.upload;

public interface OnUploadFinished {
     void onUploadError(String msg);

     void onUploadSuccess(String urlImage);
}
