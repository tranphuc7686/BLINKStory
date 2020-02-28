package com.example.blinkstory.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blinkstory.Constant;
import com.example.blinkstory.R;
import com.example.blinkstory.elements.ElementsActivity;
import com.example.blinkstory.model.entity.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Category> dataSet;
    private Context mContext;
    private int mRowHeight;
    private int lastPosition = -1;


    public CategoriesAdapter(List<Category> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mRowHeight =  displayMetrics.heightPixels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int FIRST_ELEMENT = 0;
        int sizeElement = dataSet.size() - 1;
        Category ctgData = dataSet.get(position);
        holder.category = ctgData;
        holder.txtName.setText(ctgData.getName());
        Picasso.with(mContext).load(ctgData.getUrlCategory()).placeholder(R.drawable.logoblink).into(holder.info);
         // chia ti le cua element
        if (position == FIRST_ELEMENT)
            holder.itemView.getLayoutParams().height = (mRowHeight * 40) / 100;
        else
            holder.itemView.getLayoutParams().height = ((mRowHeight * (60 / sizeElement)) / 100) - 5;
        //  start animation
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    // View lookup cache
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        ImageView info;
        Category category;

        public ViewHolder(View root) {
            super(root);
            txtName = root.findViewById(R.id.name_category);
            info = root.findViewById(R.id.item_info);
            root.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ElementsActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(Constant.CATEGORY, category);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }

    }
}