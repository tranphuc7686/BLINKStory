package com.example.blinkstory.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.blinkstory.BaseActivity;
import com.example.blinkstory.Constant;
import com.example.blinkstory.R;
import com.example.blinkstory.advertisement.BaseAdsRepository;
import com.example.blinkstory.advertisement.OnAdsListener;
import com.example.blinkstory.advertisement.RewardAdsInfo;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.ulti.FileHelper;
import com.r0adkll.slidr.Slidr;

import java.io.File;

import javax.inject.Inject;

import lombok.NonNull;

public class DetailVideoElementFragment extends Fragment implements View.OnClickListener {
    //view
    private ImageButton btnDownload;
    private ProgressBar mProgressBar;
    private View mContentView;
    private VideoView videoContent ;
    //ads
    @Inject
    @RewardAdsInfo
    private BaseAdsRepository baseAdsRepository;
    //var
    private Element element;


    public DetailVideoElementFragment() {
        // Required empty public constructor
    }

    public static DetailVideoElementFragment newInstance(@NonNull Element element) {
        DetailVideoElementFragment fragment = new DetailVideoElementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        blindListener();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((BaseActivity)getActivity()).createAdsComponent().inject(this);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_view_video_element, container, false);
        blindControlUi(view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private void blindControlUi(View view) {
        mContentView = view.findViewById(R.id.fullscreen_content);
        Slidr.attach(getActivity());
        videoContent = (VideoView) mContentView;
        btnDownload = view.findViewById(R.id.btnDownloadVideo);
        mProgressBar = view.findViewById(R.id.spinner_loading_view_element);
    }
    void init(){
        Bundle data = getArguments();
        if (data != null) {
            element = (Element) data.get(Constant.ELEMENT);
            videoContent.setVideoPath(element.getUrl());
            videoContent.start();
        }
    }


    private void blindListener() {
        btnDownload.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDownloadVideo: {
                videoContent.pause();
                baseAdsRepository.setAdvertisement(new OnAdsListener() {
                    @Override
                    public void setEarnAds() {
                        FileHelper.downloadFile(element.getUrl(), new FileHelper.OnDownloadListener() {
                            @Override
                            public void onDownloadError(String msg) {
                                ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.download_video_failed));
                            }

                            @Override
                            public void onDownloadSuccess(File file) {
                                FileHelper.saveVideoToDevice(getContext(),file);
                            }
                        });
                    }

                    @Override
                    public void setDoNotEarnAds() {
                        videoContent.start();
                        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_ads_failed));

                    }

                    @Override
                    public void setClosedAds() {
                        videoContent.start();
                        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_ads_failed));

                    }

                    @Override
                    public void onFailedLoadAds() {
                        videoContent.start();
                        ((DetailElementActivity) getActivity()).showError(getResources().getString(R.string.load_ads_failed));

                    }
                });
                break;
            }
        }
    }
}
