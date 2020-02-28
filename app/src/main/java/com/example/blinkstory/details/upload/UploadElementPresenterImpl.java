package com.example.blinkstory.details.upload;

public class UploadElementPresenterImpl implements UploadElementPresenter,OnUploadFinished {
    private UploadElementView mUploadElementView;
    private UploadElementInteractor uploadElementInteractor;


    public UploadElementPresenterImpl(UploadElementView mUploadElementView, UploadElementInteractor uploadElementInteractor) {
        this.mUploadElementView = mUploadElementView;
        this.uploadElementInteractor=uploadElementInteractor;
    }


    @Override
    public void doUploadImage(int idCategory, int typeData, String pathFile) {
        uploadElementInteractor.doUploadImageElement( idCategory, typeData, pathFile,this);
    }

    @Override
    public void doUploadVideo(int idCategory, int typeData, String pathFile) {
        uploadElementInteractor.doUploadImageElement( idCategory, typeData, pathFile,this);
    }


    @Override
    public void onUploadError(String msg) {
        mUploadElementView.showMessage(msg);
    }

    @Override
    public void onUploadSuccess(String urlImage) {
        mUploadElementView.showMessage("SUccess");
    }
}

