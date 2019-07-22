package com.example.blinkstory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blinkstory.adapter.StaggerdSpacesItemDecoration;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.adapter.ElementRecyclerViewAdapter;
import com.example.blinkstory.presenter.IElementPresenter;
import com.example.blinkstory.presenter.IElementPresenterImpl;
import com.example.blinkstory.presenter.OnLoadMoreListener;
import com.example.blinkstory.presenter.RecyclerViewLoadMoreScroll;
import com.example.blinkstory.view.IElementView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElementActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, IElementView {

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
    // pull to refesh
    private SwipeRefreshLayout pullToRefresh;

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
    private static final int PICK_FROM_GALLERY = 777;

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
        int idCtg = myIntent.getIntExtra(MainConstant.CATEGORY_ID_EXTRA, 0);
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
        int spacingInPixels = 15;
        recyclerView.addItemDecoration(new StaggerdSpacesItemDecoration(spacingInPixels));

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //set adapter
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gridLayoutManager);
        staggeredAdapter = new ElementRecyclerViewAdapter(getApplicationContext(), elements);
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
        staggeredAdapter.addLoadingView();
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                new IElementPresenterImpl(new IElementView() {
                    @Override
                    public void onGetDataElementRespone(ArrayList<Element> elements) {
                        staggeredAdapter.removeLoadingView();
                        staggeredAdapter.addData(elements);
                        staggeredAdapter.notifyDataSetChanged();
                        scrollListener.setLoaded();
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
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        pullToRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
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

    }

    private void addListener() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ElementActivity.this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String[] singleChoiceItems = {"Image", "Video"};
                final int[] itemSelected = {0};
                alertDialogBuilder
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
                                if ( itemSelected[0] == PICK_IMAGE_FILE) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
                                } else {
                                    Intent intent = new Intent();
                                    intent.setType("video/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        //setting an setOnRefreshListener on the SwipeDownLayout
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {
                //Here you can update your data from internet or from local SQLite data
//                sList.add(new Element("0HEEGA6LK9YYKHUG", "Dr Stephen Hawking", "https://instagram.fsgn2-3.fna.fbcdn.net/vp/92bc1ab09e61a31a9a80618c8c824588/5D9EE11D/t51.2885-15/e35/s1080x1080/61896010_2072686526192657_4529318621338531302_n.jpg?_nc_ht=instagram.fsgn2-3.fna.fbcdn.net", 0, "https://instagram.fsgn2-3.fna.fbcdn.net/vp/f3831face52b24f1f20e3c7d9d68a483/5D88FFD9/t51.2885-15/sh0.08/e35/s640x640/61896010_2072686526192657_4529318621338531302_n.jpg?_nc_ht=instagram.fsgn2-3.fna.fbcdn.net"));
//                staggeredAdapter.notifyDataSetChanged();
//                pullToRefresh.setRefreshing(false);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == PICK_FROM_GALLERY) {
            Uri aa = data.getData();
            String mVideoURI = Uri.parse(String.valueOf(aa)).getPath();
            System.out.println(mVideoURI+ " fjpdoskfpsofđ");
        }
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
        Toast.makeText(this, "Error " + mess, Toast.LENGTH_SHORT).show();
    }


}