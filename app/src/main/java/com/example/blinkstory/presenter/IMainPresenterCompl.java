package com.example.blinkstory.presenter;

import android.content.Context;

import com.example.blinkstory.model.entity.Category;
import com.example.blinkstory.model.entity.ICategoryImpl;
import com.example.blinkstory.model.repository.ICategory;
import com.example.blinkstory.view.IMainView;

import java.util.ArrayList;

public class IMainPresenterCompl implements IMainPresenter {
    IMainView iMainView;
    ICategory iCategory;
    Context context;

    public IMainPresenterCompl(IMainView iMainView,Context context) {
        this.iMainView = iMainView;
        this.context = context;
        iCategory = new ICategoryImpl(this);


    }
    public void loadData(){
        iCategory.getCategoryApi(context);
    }

    @Override
    public void getDataListViewCtg(ArrayList<Category> categories) {
        iMainView.onSetProgressBarVisibility();
        iMainView.onDataResponse(categories);
        iMainView.onSetProgressBarGone();
    }

    @Override
    public void onErrorCtgApi(String statusError) {
        iMainView.onDataSponseError(statusError);
    }


}
