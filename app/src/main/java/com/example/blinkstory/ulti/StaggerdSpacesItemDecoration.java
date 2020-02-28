package com.example.blinkstory.ulti;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Piyush on 1/11/2017.
 */

public class StaggerdSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public StaggerdSpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = mSpace;
    }
}