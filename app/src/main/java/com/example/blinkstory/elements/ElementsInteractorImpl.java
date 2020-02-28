package com.example.blinkstory.elements;

import com.example.blinkstory.details.upload.UploadElementPresenter;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.model.entity.ElementWrapper;
import com.example.blinkstory.network.Webservice;
import com.example.blinkstory.network.model.Paging;

import java.util.List;

import io.reactivex.Observable;

public class ElementsInteractorImpl implements ElementsInteractor {
    private UploadElementPresenter uploadElementPresenter;
    private Webservice webservice;

    public ElementsInteractorImpl(Webservice webservice) {
        this.webservice = webservice;
    }


    @Override
    public Observable<List<Element>> onGetElementApi(int idCategory, int index) {
        return webservice.elements(new Paging(idCategory,index)).map(ElementWrapper::getElementList);


    }
}








