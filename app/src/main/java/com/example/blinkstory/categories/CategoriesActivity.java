package com.example.blinkstory.categories;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blinkstory.BaseActivity;
import com.example.blinkstory.BaseApplication;
import com.example.blinkstory.R;
import com.example.blinkstory.model.entity.Category;

import java.util.List;

import javax.inject.Inject;


public class CategoriesActivity extends BaseActivity implements CategoriesView, View.OnClickListener {

    @Inject
    CategoriesPresenter categoriesPresenter;
    private RecyclerView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageButton mImageButton;
    private ProgressBar mProgressBar;


    private LinearLayout viewDonate,viewFanpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inject
        ((BaseApplication) getApplication()).createCategoriesComponent().inject(this);
        categoriesPresenter.setView(this);
        categoriesPresenter.getDataListViewCtg();
        blindUI();
        blindListener();


    }

    private void blindUI() {
        mProgressBar = findViewById(R.id.spinner_loading_main);
        listView = findViewById(R.id.list);
        /*for navigation drawer*/
        mImageButton =  findViewById(R.id.btnNavigationDra);
        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dummy_content, R.string.dummy_content);
        drawerLayout.addDrawerListener(drawerToggle);
        viewDonate = findViewById(R.id.navDonate);
        viewFanpage =findViewById(R.id.navFanpage);
    }
    void blindListener() {
        viewFanpage.setOnClickListener(this);
        mImageButton.setOnClickListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dummy_content, R.string.dummy_content);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about:
                Toast.makeText(this, "About button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(this, "Help button selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onDataResponse(List<Category> categories) {
        mProgressBar.setVisibility(View.GONE);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new CategoriesAdapter(categories, this));
    }


    @Override
    public void onDataSponseError(String status) {
        mProgressBar.setVisibility(View.GONE);
        showError("Error connection !! Pleases check your connection.");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navFanpage: {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(view.getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;
            }
            case R.id.btnNavigationDra: {
                drawerLayout.openDrawer(Gravity.START);
                break;
            }
        }
    }

    public String getFacebookPageURL(Context context) {
        final String FACEBOOK_PAGE_ID = "346381369567878";
        final String FACEBOOK_URL = "https://www.facebook.com/Blinks-346381369567878/";

        if (appInstalledOrNot(context, "com.facebook.katana")) {
            return "fb://page/" + FACEBOOK_PAGE_ID;
        }
        return FACEBOOK_URL;

    }

    boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}


