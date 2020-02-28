package com.example.blinkstory.categories;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CategoriesPresenterImpl implements CategoriesPresenter {
    CategoriesView categoriesView;
    CategoriesInteractor categoriesInteractor;

    public CategoriesPresenterImpl(CategoriesInteractor categoriesInteractor) {
        this.categoriesInteractor =categoriesInteractor;
    }


    @Override
    public void getDataListViewCtg() {
        categoriesInteractor.getCategoryApi().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    categoriesView.onDataResponse(e);
                }, t -> {
                    categoriesView.onDataSponseError(t.getMessage());
                });

    }

    @Override
    public void onErrorCtgApi(String statusError) {
        categoriesView.onDataSponseError(statusError);
    }

    @Override
    public void setView(CategoriesView view) {
        categoriesView = view;
    }


}
