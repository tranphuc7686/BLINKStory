package com.example.blinkstory.details.upload;

public interface UploadElementPresenter {
    void doUploadImage( int idCategory, int typeData, String pathFile);
    void doUploadVideo(int idCategory, int typeData, String pathFile);
}
