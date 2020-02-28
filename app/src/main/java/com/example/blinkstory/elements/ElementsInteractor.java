package com.example.blinkstory.elements;

import com.example.blinkstory.model.entity.Element;

import java.util.List;

import io.reactivex.Observable;

public interface ElementsInteractor {
    Observable<List<Element>> onGetElementApi(int idCategory, int index);


}
