package com.example.blinkstory.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.blinkstory.BaseActivity;
import com.example.blinkstory.Constant;
import com.example.blinkstory.R;
import com.example.blinkstory.advertisement.BaseAdsRepository;
import com.example.blinkstory.advertisement.OnAdsListener;
import com.example.blinkstory.advertisement.RewardAdsInfo;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.ulti.FileHelper;
import com.example.blinkstory.ulti.ViewHelper;
import com.r0adkll.slidr.Slidr;

import javax.inject.Inject;

import lombok.NonNull;

public class DetailImageElementFragment extends Fragment implements View.OnClickListener, ViewHelper.OnBackGroundContentListener {

    private View mControlsView;
    private ProgressBar spinnerLoading;
    private View mBackgroundImage;
    private View mContentView;
    private ImageButton btnSetWallpaper, btnDownload;
    private ImageView imageViewContent;

    // var
    private String urlDisplay;
    private Bitmap bitmapImgContent;
    private String pathImageToChangeWallpater;
    // ads
    @Inject
    @RewardAdsInfo
    BaseAdsRepository baseAdsRepository;
    public DetailImageElementFragment() {
        // Required empty public constructor
    }

    public static DetailImageElementFragment newInstance(@NonNull Element element) {

        DetailImageElementFragment fragment = new DetailImageElementFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ELEMENT, element);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void blindListenerUi() {
        btnSetWallpaper.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((BaseActivity)getActivity()).createAdsComponent().inject(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_view_element, container, false);
        blindControlUi(view);
        blindListenerUi();
        init();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CHANGE_WALLPAPER) {
            getActivity().getContentResolver().delete(Uri.parse(pathImageToChangeWallpater), null, null);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetWallpaper: {
                baseAdsRepository.setAdvertisement(new OnAdsListener() {
                    @Override
                    public void setEarnAds() {
                        pathImageToChangeWallpater = FileHelper.getPathFileImageInDevice(getContext(), bitmapImgContent);
                        Intent activitySetWallpaper = FileHelper.redirectActivitySetWallpaper(Uri.parse(pathImageToChangeWallpater));
                        startActivityForResult(activitySetWallpaper, Constant.REQUEST_CHANGE_WALLPAPER);

                    }

                    @Override
                    public void setDoNotEarnAds() {
                        ((DetailElementActivity) getActivity()).showError("Failed to set wallpaper !!");

                    }

                    @Override
                    public void setClosedAds() {
                        pathImageToChangeWallpater = FileHelper.getPathFileImageInDevice(getContext(), bitmapImgContent);
                        Intent activitySetWallpaper = FileHelper.redirectActivitySetWallpaper(Uri.parse(pathImageToChangeWallpater));
                        startActivityForResult(activitySetWallpaper, Constant.REQUEST_CHANGE_WALLPAPER);

                    }

                    @Override
                    public void onFailedLoadAds() {
                        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_backgound_detail_failed));

                    }
                });
                break;
            }
            case R.id.btnDownload: {
                baseAdsRepository.setAdvertisement(new OnAdsListener() {
                    @Override
                    public void setEarnAds() {
                        FileHelper.saveImageToGallery(getContext(), bitmapImgContent);

                    }

                    @Override
                    public void setDoNotEarnAds() {
                        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_ads_failed));
                    }

                    @Override
                    public void setClosedAds() {
                        ((DetailElementActivity) getActivity()).showError("Failed to download Image !!");

                    }

                    @Override
                    public void onFailedLoadAds() {
                        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_ads_failed));

                    }
                });
                break;
            }
        }
    }

    private void blindControlUi(View view) {
        mControlsView = view.findViewById(R.id.fullscreen_content_controls);
        mContentView = view.findViewById(R.id.fullscreen_content);
        mBackgroundImage = view.findViewById(R.id.background_content);
        spinnerLoading = view.findViewById(R.id.spinner_loading_view_element);
        btnSetWallpaper = view.findViewById(R.id.btnSetWallpaper);
        btnDownload = view.findViewById(R.id.btnDownload);
        imageViewContent = (ImageView) mContentView;
        // swipe left to back activity
        Slidr.attach(getActivity());

    }

    private void init() {
        Bundle data = getArguments();
        if (data != null) {
            Element element = (Element) data.get(Constant.ELEMENT);
            ViewHelper.setImageBackGroundContent(getContext(), mBackgroundImage, imageViewContent, element.getUrl(), this);
        }

    }


    @Override
    public void onSetBitmap(Bitmap bitmap) {
        imageViewContent.setImageBitmap(bitmap);
    }

    @Override
    public void onGetBitmapFailed() {
        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_backgound_detail_failed));
    }


}
