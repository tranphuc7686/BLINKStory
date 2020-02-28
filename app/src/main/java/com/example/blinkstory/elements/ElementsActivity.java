package com.example.blinkstory.elements;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.blinkstory.BaseActivity;
import com.example.blinkstory.BaseApplication;
import com.example.blinkstory.Constant;
import com.example.blinkstory.R;
import com.example.blinkstory.details.DetailElementActivity;
import com.example.blinkstory.details.upload.UploadElementPresenter;
import com.example.blinkstory.details.upload.UploadElementView;
import com.example.blinkstory.model.entity.Category;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.ulti.FileHelper;
import com.example.blinkstory.ulti.RecyclerViewLoadMoreScroll;
import com.example.blinkstory.ulti.StaggerdSpacesItemDecoration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElementsActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, ElementsView, UploadElementView, ElementsAdapter.OnItemClickListener,View.OnClickListener {

    // Presenter
    @Inject
    ElementsPresenter elementsPresenter;
    private UploadElementPresenter uploadElementPresenter;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private final int FIRST_PAGE = 0;
    private static final int PICK_IMAGE_FILE = 0;
    private static final int PICK_VIDEO_FILE = 0;
    private static final int PICK_FROM_GALLERY_IMAGE = 777;
    private static final int PICK_FROM_GALLERY_VIDEO = 778;

    // View
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private FloatingActionButton fab;
    private SlidrInterface slidr;
    private ProgressBar spinner;
    private RecyclerView recyclerView;
    private TextView nameCtg;
    private CircleImageView mCircleImageView;

    //var
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private int idCtg;
    //ads
    private InterstitialAd mInterstitialAd;
    private AdView adView;
    //helper
    private RecyclerViewLoadMoreScroll scrollListener;
    private ElementsAdapter staggeredAdapter;
    private StaggeredGridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        //depency inject
        ((BaseApplication)getApplication()).createElementComponent().inject(this);
        elementsPresenter.setView(this);
        //
        addControl();
        addListener();
    }

    private void init() {
        Category ctg = getIntent().getExtras().getParcelable(Constant.CATEGORY);
        idCtg = ctg.getIdCategory();
        nameCtg.setText(ctg.getName());
        mTitle.setText(ctg.getName());
        Picasso.with(getApplicationContext())
                .load(ctg.getUrlCategory())
                .into(this.mCircleImageView);
        spinner.setVisibility(View.VISIBLE);
        elementsPresenter.setDataElements(idCtg, FIRST_PAGE);

    }

    private void SetupRecyclerView(ArrayList<Element> elements) {
        int spacingInPixels = 15;

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.addItemDecoration(new StaggerdSpacesItemDecoration(spacingInPixels));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //set adapter
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        staggeredAdapter = new ElementsAdapter(getApplicationContext(), elements, this);
        staggeredAdapter.setHasStableIds(true);
        recyclerView.setAdapter(staggeredAdapter);

        scrollListener = new RecyclerViewLoadMoreScroll(gridLayoutManager);
        scrollListener.setOnLoadMoreListener(this::LoadMoreData);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void LoadMoreData(int index) {
        recyclerView.setLayoutFrozen(true);
        staggeredAdapter.addLoadingView();
        //add loading item
        new Handler().post(() -> elementsPresenter.setLoadMoreDataElements(1, index, new OnLoadMoreListenner() {
            @Override
            public void setDataLoadMore(ArrayList<Element> elements) {
                if (elements.size() == 0) {
                    recyclerView.setLayoutFrozen(false);
                    return;
                }

                staggeredAdapter.removeLoadingView();
                staggeredAdapter.addData(elements);
                staggeredAdapter.notifyDataSetChanged();
                scrollListener.setLoaded();
                recyclerView.setLayoutFrozen(false);
            }

            @Override
            public void getMsgErrorLoadMore(String msg) {
                showError(msg);
            }
        }));


    }


    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


    private void addControl() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        spinner = (ProgressBar) findViewById(R.id.spinner_loading_element);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.ctgTitle);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        slidr = Slidr.attach(this);
        nameCtg = findViewById(R.id.nameCtg);
        mCircleImageView = (CircleImageView) findViewById(R.id.ctgThub);
        //add ads
        adView = findViewById(R.id.adBannerView);
        init();
        addAds(this);

    }

    private void addListener() {
        fab.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Uri aa = data.getData();
        String mVideoURI = FileHelper.getPath(this, aa);
        if (requestCode == PICK_FROM_GALLERY_IMAGE) {
            uploadElementPresenter.doUploadImage(idCtg, Constant.TYPE_IMAGE, mVideoURI);
            return;
        }
        uploadElementPresenter.doUploadVideo(idCtg, Constant.TYPE_VIDEO, mVideoURI);
        // Get Path of selected image

    }



    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }


    @Override
    public void onGetDataElementRespone(ArrayList<Element> elements) {
        spinner.setVisibility(View.GONE);
        SetupRecyclerView(elements);
    }

    @Override
    public void onItemClick(Element element) {
//        if (mInterstitialAd.isLoaded() && (Constant.generateRandomIntIntRange(1, 5) == 2)) {
////            mInterstitialAd.show();
////            mInterstitialAd.setAdListener(new AdListener() {
////                @Override
////                public void onAdLoaded() {
////                    super.onAdLoaded();
////                }
////
////                @Override
////                public void onAdClosed() {
////                    super.onAdClosed();
////                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
////                    /* Create an Intent that will start the Menu-Activity. */
////                    redirectToActivity(element);
////
////                }
////
////                @Override
////                public void onAdFailedToLoad(int i) {
////                    super.onAdFailedToLoad(i);
////                    /* Create an Intent that will start the Menu-Activity. */
////                    redirectToActivity(element);
////
////                }
////            });
////        } else {
////            redirectToActivity( element);
////        }
        redirectToActivity( element);
    }

    private void redirectToActivity(Element element) {
        Intent intent  = new Intent( getApplicationContext(), DetailElementActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constant.ELEMENT, element);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void addAds(Context mContext) {
        //add ads
        MobileAds.initialize(mContext,
                Constant.APP_ADS);
        adView.loadAd(new AdRequest.Builder().build());
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(Constant.XENKE_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:{
                final String[] singleChoiceItems = {"Image", "Video"};
                final int[] itemSelected = {0};
                new AlertDialog.Builder(ElementsActivity.this)
                        .setTitle("Select your data import !!")
                        .setSingleChoiceItems(singleChoiceItems, itemSelected[0], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {

                                itemSelected[0] = selectedIndex;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (itemSelected[0] == PICK_IMAGE_FILE) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY_IMAGE);
                                } else {
                                    Intent intent = new Intent();
                                    intent.setType("video/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY_VIDEO);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;
            }
        }
    }
}