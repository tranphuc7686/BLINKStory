package com.example.blinkstory;

public interface BaseView {
    void showNetWorkDisconnected();
    void showLoadingProcess();
    void hideLoadingPeocess();
    void showError(String msg);
    void showMessage(String msg);
}
