package com.example.blinkstory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jackandphantom.blurimage.BlurImage;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewImageElementActivity extends AppCompatActivity {
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

    //button
    private ImageButton btnSetWallpaper,btnDownload;
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
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private SlidrInterface slidr;
    private int BLUR_PRECENTAGE = 90;
    private View mBackgroundImage;
    private ProgressBar spinnerLoading;

    private Bitmap imgContent;
    private String urlDisplay;
    private String pathImageToChangeWallpater;
    // reques code to change wallpaper activity
    private static final int REQUEST_CHANGE_WALLPAPER = 0x9345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_element);
        requestAppPermissions();
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mBackgroundImage = findViewById(R.id.background_content);
        //blind UI
        blindControlUi();
        //
        blindListenerUi();

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button1).setOnTouchListener(mDelayHideTouchListener);
        // swipe left to back activity
        slidr = Slidr.attach(this);
        //get data from element activity
        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent
        urlDisplay = myIntent.getStringExtra("UrlDisplay");
        //set main image content
        ImageView imageViewContent = (ImageView) mContentView;
        setImageBackGroundContent(getApplicationContext(),mBackgroundImage,imageViewContent,urlDisplay);

    }
    private void blindControlUi(){
        spinnerLoading = (ProgressBar) findViewById(R.id.spinner_loading_view_element);
        btnSetWallpaper = (ImageButton) findViewById(R.id.btnSetWallpaper);
        btnDownload = (ImageButton) findViewById(R.id.btnDownload);
    }
    private void blindListenerUi() {
        btnSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLick to set wallpaper !!!");
                setWallpaper();
            }

            private void setWallpaper() {
                Uri image=getImageUri(getApplicationContext(),imgContent);
                //Start Crop Activity
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                //can't use normal URI, because it requires the Uri from file
                intent.setDataAndType(image,"image/*");
                intent.putExtra("mimeType","image/*");
                startActivityForResult(Intent.createChooser(intent,"Set Image"),REQUEST_CHANGE_WALLPAPER);
                /*set wallpaper method*/
//                DisplayMetrics metrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                int height = metrics.heightPixels;
//                int width = metrics.widthPixels;
//                Bitmap bitmap = Bitmap.createScaledBitmap(imgContent, width, height, true);
//
//                WallpaperManager wallpaperManager = WallpaperManager.getInstance(ViewImageElementActivity.this);
//                try {
//                    wallpaperManager.setBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                /*Crop image*/
//                try {
//                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
//                    // indicate image type and Uri
//                    Uri contentUri = Uri.parse(urlDisplay);
//
//                    cropIntent.setDataAndType(contentUri, "image/*");
//                    // set crop properties
//                    cropIntent.putExtra("crop", "true");
//                    // indicate aspect of desired crop
//                    cropIntent.putExtra("aspectX", 1);
//                    cropIntent.putExtra("aspectY", 1);
//                    // indicate output X and Y
//                    cropIntent.putExtra("outputX", 280);
//                    cropIntent.putExtra("outputY", 280);
//
//                    // retrieve data on return
//                    cropIntent.putExtra("return-data", true);
//                    // start the activity - we handle returning in onActivityResult
//                    startActivityForResult(cropIntent, 400);
//                }
//                catch (ActivityNotFoundException anfe) {
//                    // display an error message
//                    String errorMessage = "your device doesn't support the crop action!";
//                    Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
//                    toast.show();
//                }
            }
            private Uri getImageUri(Context context, Bitmap inImage) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                pathImageToChangeWallpater = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);

                return Uri.parse(pathImageToChangeWallpater);
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg();
            }

            private void downloadImg() {
/* get image from galley*/
//                Intent i=
//                        new Intent()
//                                .setType("image/png")
//                                .setAction(Intent.ACTION_GET_CONTENT)
//                                .addCategory(Intent.CATEGORY_OPENABLE);
//
//                startActivityForResult(i, 1);
                saveImageToGallery(getApplicationContext(),imgContent);










            }
            private void saveImageToGallery(Context context, Bitmap inImage) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
                Toast.makeText(context, "Save at : "+path, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CHANGE_WALLPAPER) {
            getContentResolver().delete(Uri.parse(pathImageToChangeWallpater), null,null);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void setImageBackGroundContent(final Context context, final View mBackgroundImage, final ImageView imageContent, final String url){
        spinnerLoading.setVisibility(View.VISIBLE);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                imgContent = bitmap;
                /* Save the bitmap or do something with it here */

                //Set it in the ImageView
                Bitmap bitmap2 = BlurImage.with(getApplicationContext()).load(bitmap).intensity(25).Async(true).getImageBlur();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBackgroundImage.setBackground(new BitmapDrawable(getResources(), bitmap2));
                }
                Picasso.with(context).load(url).into(imageContent);
                spinnerLoading.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                System.out.println("onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                System.out.println("onPrepareLoad");
            }
        };
        imageContent.setTag(target);
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.logo)
                .into(target);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ondestroy !!");
    }
}
