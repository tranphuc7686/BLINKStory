package com.example.blinkstory.ulti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.example.blinkstory.R;
import com.jackandphantom.blurimage.BlurImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ViewHelper {
    public static void setImageBackGroundContent(Context context,View mBackgroundImage,ImageView imageContent,String url,OnBackGroundContentListener onBackGroundContentListener){

        Target target = new Target() {
            @Override
            public void onBitmapLoaded ( Bitmap bitmap, Picasso.LoadedFrom from){
                onBackGroundContentListener.onSetBitmap(bitmap);
                Bitmap bitmap2 = BlurImage.with(context).load(bitmap).intensity(25).Async(true).getImageBlur();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBackgroundImage.setBackground(new BitmapDrawable(context.getResources(), bitmap2));
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                onBackGroundContentListener.onGetBitmapFailed();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        imageContent.setTag(target);
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.logoblink)
                .into(target);

    }

    public interface OnBackGroundContentListener{
        void onSetBitmap( Bitmap bitmap);
        void onGetBitmapFailed();
    }

}
