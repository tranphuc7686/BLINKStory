package com.example.blinkstory;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.blinkstory.advertisement.AdsComponent;
import com.example.blinkstory.advertisement.AdsModule;
import com.example.blinkstory.ulti.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity implements BaseView {
    protected ActivityComponent activityComponent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }
        hideSystemUI();
        activityComponent = DaggerActivityComponent.builder()
                .build();
    }
    public AdsComponent createAdsComponent(){
        return activityComponent.plus(new AdsModule(this));
    }

    @Override
    public void showNetWorkDisconnected() {
        if(!NetworkUtils.isNetworkConnected(getApplicationContext())){
            Snackbar.make(getWindow().getDecorView().getRootView(), R.string.check_connection, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }
    @Override
    public void showLoadingProcess() {

    }

    @Override
    public void hideLoadingPeocess() {

    }

    @Override
    public void showError(String msg) {
        Snackbar.make(getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    public void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 777); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

}
