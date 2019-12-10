package com.example.blinkstory;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.blinkstory.adapter.ElementRecyclerViewAdapter;
import com.example.blinkstory.adapter.OnItemClickListener;
import com.example.blinkstory.adapter.StaggerdSpacesItemDecoration;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.presenter.IElementPresenter;
import com.example.blinkstory.presenter.IElementPresenterImpl;
import com.example.blinkstory.presenter.OnLoadMoreListener;
import com.example.blinkstory.presenter.RecyclerViewLoadMoreScroll;
import com.example.blinkstory.presenter.UploadFilePresenter;
import com.example.blinkstory.view.IElementView;
import com.example.blinkstory.view.IUploadView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElementActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, IElementView, IUploadView, OnItemClickListener {

    private StaggeredGridLayoutManager gridLayoutManager;
    //setup toolbar
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private FloatingActionButton fab;
    private SlidrInterface slidr;
    // spinner loading
    private ProgressBar spinner;

    // Presenter
    private IElementPresenter iElementPresenter;

    private RecyclerViewLoadMoreScroll scrollListener;
    private ElementRecyclerViewAdapter staggeredAdapter;
    private RecyclerView recyclerView;
    //
    private final int FIRST_PAGE = 0;
    //
    private TextView nameCtg;
    private CircleImageView mCircleImageView;
    private static final int PICK_IMAGE_FILE = 0;
    private static final int PICK_VIDEO_FILE = 0;
    private static final int PICK_FROM_GALLERY_IMAGE = 777;
    private static final int PICK_FROM_GALLERY_VIDEO = 778;
    //id category
    private int idCtg;
    //ads
    private InterstitialAd mInterstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }

        setContentView(R.layout.activity_element);

        addControl();

        addListener();

        init();

    }

    private void init() {
        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent
        idCtg = myIntent.getIntExtra(MainConstant.CATEGORY_ID_EXTRA, 0);
        String thubCtg = myIntent.getStringExtra(MainConstant.CATEGORY_THUBMAIL_EXTRA);
        String nameCtg = myIntent.getStringExtra(MainConstant.CATEGORY_NAME_EXTRA);
        this.nameCtg.setText(nameCtg);
        this.mTitle.setText(nameCtg);
        Picasso.with(getApplicationContext())
                .load(thubCtg)
                .into(this.mCircleImageView);
        iElementPresenter = new IElementPresenterImpl(this, getApplicationContext(), spinner);
        ((IElementPresenterImpl) iElementPresenter).setLoadDataElement(idCtg, FIRST_PAGE);

    }

    private void SetupRecyclerView(ArrayList<Element> elements) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        int spacingInPixels = 15;
        recyclerView.addItemDecoration(new StaggerdSpacesItemDecoration(spacingInPixels));

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //set adapter
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gridLayoutManager);
        staggeredAdapter = new ElementRecyclerViewAdapter(getApplicationContext(), elements,this);
        staggeredAdapter.setHasStableIds(true);
        recyclerView.setAdapter(staggeredAdapter);

        scrollListener = new RecyclerViewLoadMoreScroll(gridLayoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int index) {

                LoadMoreData(index);
            }
        });

        recyclerView.addOnScrollListener(scrollListener);
    }

    private void LoadMoreData(final int index) {
        recyclerView.setLayoutFrozen(true);
        staggeredAdapter.addLoadingView();
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                new IElementPresenterImpl(new IElementView() {
                    @Override
                    public void onGetDataElementRespone(ArrayList<Element> elements) {

                        if(elements.size() == 0){
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
                    public void onGetDataElementErrorListener(String mess) {

                    }


                }, getApplicationContext(), spinner).setLoadDataElement(1, index);


            }
        });


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

    private void addControl() {
        fab = (FloatingActionButton) findViewById(R.id.fab);

        spinner = (ProgressBar) findViewById(R.id.spinner_loading_element);


//        spinner.setColorSchemeResources(R.color.colorAccent,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.ctgTitle);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        hideSystemUI();
        slidr = Slidr.attach(this);
        //
        nameCtg = (TextView) findViewById(R.id.nameCtg);
        mCircleImageView = (CircleImageView) findViewById(R.id.ctgThub);
        //add ads
        adView = findViewById(R.id.adBannerView);
        addAds(this);

    }

    private void addListener() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ElementActivity.this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                final String[] singleChoiceItems = {"Image", "Video"};
//                final int[] itemSelected = {0};
//                alertDialogBuilder
//                        .setTitle("Select your data import !!")
//                        .setSingleChoiceItems(singleChoiceItems, itemSelected[0], new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
//
//                                itemSelected[0] = selectedIndex;
//                            }
//                        })
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if (itemSelected[0] == PICK_IMAGE_FILE) {
//                                    Intent intent = new Intent();
//                                    intent.setType("image/*");
//                                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY_IMAGE);
//                                } else {
//                                    Intent intent = new Intent();
//                                    intent.setType("video/*");
//                                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY_VIDEO);
//                                }
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .show();
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IUploadView iUploadView = this;
        if (resultCode != RESULT_OK) return;
        Uri aa = data.getData();
        String mVideoURI = getPath(this, aa);
        if (requestCode == PICK_FROM_GALLERY_IMAGE) {
            new UploadFilePresenter(iUploadView, spinner, getApplicationContext()).setOnUploadFileTask(idCtg,MainConstant.TYPE_IMAGE, mVideoURI);
            return;
        }
        new UploadFilePresenter(iUploadView, spinner, getApplicationContext()).setOnUploadFileTask(idCtg,MainConstant.TYPE_VIDEO, mVideoURI);
        // Get Path of selected image

    }



    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {

            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");

            final String docId = dat[1];
            final String type = dat[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onGetDataElementRespone(ArrayList<Element> elements) {
        SetupRecyclerView(elements);
    }

    @Override
    public void onGetDataElementErrorListener(String mess) {

        Toast.makeText(this, "Error connection !! Pleases check your connection. ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSusscess() {
        Snackbar.make(getWindow().getDecorView().getRootView(), "Upload Success !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onFailed(String msg) {
        Snackbar.make(getWindow().getDecorView().getRootView(), "Upload Failed : " + msg + " !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onItemClick(final View view, final Element element) {
        if(mInterstitialAd.isLoaded()&& (MainConstant.generateRandomIntIntRange(1,5) == 2)){
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    /* Create an Intent that will start the Menu-Activity. */
                    redriectToActivity(view,element);

                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    /* Create an Intent that will start the Menu-Activity. */
                    redriectToActivity(view,element);

                }
            });
        }
        else{
            redriectToActivity(view,element);
        }
    }
    private void redriectToActivity(View view, Element element){
         final int TYPE_VIDEO = 1;
        Intent intent = null;
        if(element.getTypeData() == TYPE_VIDEO){
            intent = new Intent (view.getContext(), ViewVideoElementActivity.class);
            intent.putExtra("UrlDisplay",element.getUrl() );
            intent.putExtra("NameContentVideo",element.getId() );
            view.getContext().startActivity(intent);
            return;
        }
        intent = new Intent (view.getContext(), ViewImageElementActivity.class);
        intent.putExtra("UrlDisplay",element.getUrl() );
        view.getContext().startActivity(intent);
    }
    private void addAds(Context mContext){
        //add ads
        MobileAds.initialize(mContext,
                MainConstant.APP_ADS);
        adView.loadAd(new AdRequest.Builder().build());
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(MainConstant.XENKE_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }
}