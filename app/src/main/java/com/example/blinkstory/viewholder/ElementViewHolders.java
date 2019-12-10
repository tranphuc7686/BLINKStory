package com.example.blinkstory.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blinkstory.model.entity.Element;
import com.google.android.gms.ads.InterstitialAd;
import com.example.blinkstory.R;

public class ElementViewHolders extends RecyclerView.ViewHolder {

    public ImageView elementImg;
    public ImageView iconVideo;
    public Element element;
    private InterstitialAd mInterstitialAd;
    public View container;
    public ElementViewHolders(View itemView)
    {
        super(itemView);
        container = itemView;
        elementImg = (ImageView) itemView.findViewById(R.id.element_image);
        iconVideo = (ImageView) itemView.findViewById(R.id.icon_video_element);

    }



}