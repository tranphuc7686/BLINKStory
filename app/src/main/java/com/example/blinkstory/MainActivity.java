package com.example.blinkstory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.blinkstory.adapter.MainListViewAdapter;
import com.example.blinkstory.model.entity.Category;
import com.example.blinkstory.presenter.IMainPresenter;
import com.example.blinkstory.presenter.IMainPresenterCompl;
import com.example.blinkstory.view.IMainView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainView {
    ArrayList<Category> dataModels;
    ListView listView;
    private static MainListViewAdapter adapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageButton mImageButton;
    private ProgressBar mProgressBar;

    private IMainPresenter iMainPresenter;

    private LinearLayout viewDonate,viewFanpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide system toolbar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }
        setContentView(R.layout.activity_main);
        //full screen setting
        hideSystemUI();
        blindUI();
        blindListener();


    }

    private void blindListener() {
        viewDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewFanpage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(view.getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
            public  String getFacebookPageURL(Context context)
            {
                final String FACEBOOK_PAGE_ID = "346381369567878";
                final String FACEBOOK_URL = "https://www.facebook.com/Blinks-346381369567878/";

                if(appInstalledOrNot(context, "com.facebook.katana"))
                {
                    return "fb://page/" + FACEBOOK_PAGE_ID;
                }
                return FACEBOOK_URL;

            }
            private  boolean appInstalledOrNot(Context context, String uri)
            {
                PackageManager pm = context.getPackageManager();
                try
                {
                    pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                    return true;
                }
                catch(PackageManager.NameNotFoundException e)
                {
                }

                return false;
            }

        });
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

    private void blindUI() {
        mProgressBar = (ProgressBar) findViewById(R.id.spinner_loading_main);
        listView = (ListView) findViewById(R.id.list);
        /*for navigation drawer*/
        mImageButton = (ImageButton) findViewById(R.id.btnNavigationDra);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dummy_content, R.string.dummy_content);
        drawerLayout.addDrawerListener(drawerToggle);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        iMainPresenter = new IMainPresenterCompl(this,getApplicationContext());
        ((IMainPresenterCompl) iMainPresenter).loadData();

        //
        viewDonate = (LinearLayout)findViewById(R.id.navDonate);
        viewFanpage = (LinearLayout)findViewById(R.id.navFanpage);
    }


    @Override
    public void onDataResponse(ArrayList<Category> categories) {
        dataModels = categories;
        adapter = new MainListViewAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Category dataModel = dataModels.get(position);

                Snackbar.make(view, dataModel.getName() + "\n" + dataModel.getName() + " API: " + dataModel.getIdCategory(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    @Override
    public void onSetProgressBarVisibility() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSetProgressBarGone() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataSponseError(String status) {
        Toast.makeText(this, "Error "+status, Toast.LENGTH_SHORT).show();
    }
}


