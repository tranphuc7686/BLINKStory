package com.example.blinkstory.details.upload;

import com.example.blinkstory.Constant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.network.Webservice;

public class UploadElementInteractorImpl implements UploadElementInteractor {

    private UploadElementPresenter uploadElementPresenter;
    private UploadFileAsyntask mUploadFileAsyntask;
    private Webservice webservice;

    public UploadElementInteractorImpl(UploadFileAsyntask mUploadFileAsyntask,Webservice webservice) {

        this.mUploadFileAsyntask = mUploadFileAsyntask;
        this.webservice = webservice;
    }

    @Override
    public void doUploadVideoElement(final int idCategory, int typeData, final String pathFile, final OnUploadFinished onUploadFinished) {
        mUploadFileAsyntask.startUploadFile(UploadFileAsyntask.getThubmailVideo(pathFile), "thumnail.jpg", new UploadFileAsyntask.Callback() {
            @Override
            public void onFailed(String msg) {
                onUploadFinished.onUploadError(msg);
            }

            @Override
            public void onSuccess(String url) {
                mUploadFileAsyntask.startUploadFile(UploadFileAsyntask.getFileDataContent(pathFile), pathFile.substring(pathFile.lastIndexOf(".")), new UploadFileAsyntask.Callback() {
                    @Override
                    public void onFailed(String msg) {
                        onUploadFinished.onUploadError(msg);
                    }

                    @Override
                    public void onSuccess(String url) {
                        webservice.addElement(new Element("", "", url, Constant.TYPE_VIDEO, url));
                    }
                });
            }
        });
    }

    @Override
    public void doUploadImageElement(final int idCategory, int typeData, final String pathFile, final OnUploadFinished onUploadFinished) {
        mUploadFileAsyntask.startUploadFile(UploadFileAsyntask.getThubmailImage(pathFile), "thumnail" + pathFile.substring(pathFile.lastIndexOf(".")), new UploadFileAsyntask.Callback() {
            @Override
            public void onFailed(String msg) {
                onUploadFinished.onUploadError(msg);
            }

            @Override
            public void onSuccess(String url) {
                mUploadFileAsyntask.startUploadFile(UploadFileAsyntask.getFileDataContent(pathFile), pathFile.substring(pathFile.lastIndexOf(".")), new UploadFileAsyntask.Callback() {
                    @Override
                    public void onFailed(String msg) {
                        onUploadFinished.onUploadError(msg);
                    }

                    @Override
                    public void onSuccess(String url) {
                        webservice.addElement(new Element("", "", url, Constant.TYPE_VIDEO, url));
                    }
                });
            }
        });
    }
}
