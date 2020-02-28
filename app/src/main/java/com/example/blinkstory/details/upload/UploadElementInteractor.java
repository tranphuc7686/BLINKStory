package com.example.blinkstory.details.upload;

public interface UploadElementInteractor {
    void doUploadVideoElement( int idCategory, int typeData, String pathFile,OnUploadFinished onUploadFinished);
    void doUploadImageElement(int idCategory, int typeData, String pathFile,OnUploadFinished onUploadFinished);
}
