package com.example.blinkstory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.presenter.DownloadFilePresenter;
import com.example.blinkstory.view.IDownloadFileView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.File;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewVideoElementActivity extends AppCompatActivity implements IDownloadFileView {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;

    private boolean mVisible;
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    // slide to finish activitu
    private SlidrInterface slidr;
    //button
    private ImageButton btnDownload;
    //process bar
    private ProgressBar mProgressBar;
    //ads
    private RewardedAd rewardedAd;
    //video view
    VideoView videoContent ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAppPermissions();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }
        setContentView(R.layout.activity_view_video_element);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        blindControlUi();
        blindListenerUi();
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
    private void blindListenerUi() {
        final IDownloadFileView mIDownloadFileView = this;
        btnDownload.setOnClickListener(new View.OnClickListener() {
            boolean isEarn = false;
            @Override
            public void onClick( View view) {
                videoContent.pause();
                //funciton show ads
                if (rewardedAd.isLoaded()) {
                    Activity activityContext = ViewVideoElementActivity.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            if(!isEarn){
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Download failed !! ", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("DISMISS", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        }).show();

                            }
                            videoContent.start();
                            // Ad closed.
                            rewardedAd = createAndLoadRewardedAd() ;
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            isEarn = true;
                            // User earned reward.
                            //funciton download data
                            downloadVideo();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Download failed, check your internet !! ", Snackbar.LENGTH_INDEFINITE)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }


            }
            private void downloadVideo() {
                new DownloadFilePresenter(mIDownloadFileView,getIdData(),getIntentData(),mProgressBar)
                        .setOnDownloadTask();
            }


        });
    }
    private void requestAppPermissions() {
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
    private void blindControlUi() {//set main image content
        hideSystemUI();
        //new acti
        slidr = Slidr.attach(this);
        videoContent = (VideoView) mContentView;
        videoContent.setVideoPath(getIntentData());
        videoContent.start();
        btnDownload = (ImageButton)findViewById(R.id.btnDownloadVideo);
        mProgressBar = (ProgressBar)findViewById(R.id.spinner_loading_view_element);
        addAds();

    }
    private String getIntentData(){
        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent
        return myIntent.getStringExtra("UrlDisplay");
    }
    private String getIdData(){
        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent
        return myIntent.getStringExtra("NameContentVideo");
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }

    @Override
    public void onSusscess(File file) {
        addVideo(file);
        Snackbar.make(getWindow().getDecorView().getRootView(), "Download complete !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onFailed(String msg) {
        Snackbar.make(getWindow().getDecorView().getRootView(), "Download Failed !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public Uri addVideo(File videoFile) {
        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(videoFile.getAbsolutePath()));
        int duration = mp.getDuration();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, "My video title");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        values.put(MediaStore.Video.Media.DURATION, duration);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);


    }
    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void addAds() {
        MobileAds.initialize(this, MainConstant.APP_ADS);
        this.rewardedAd = createAndLoadRewardedAd();
    }
    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                MainConstant.TRATHUONG_ADS);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }
}
