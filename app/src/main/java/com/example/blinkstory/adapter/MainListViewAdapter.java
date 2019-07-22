package com.example.blinkstory.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.blinkstory.ElementActivity;
import com.example.blinkstory.R;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainListViewAdapter extends ArrayAdapter<Category> implements View.OnClickListener{

    private ArrayList<Category> dataSet;
    Context mContext;
    private int mRowHeight = 0;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView info;
    }

    public MainListViewAdapter(ArrayList<Category> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        mRowHeight = height;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Category dataModel=(Category)object;

        switch (v.getId())
        {
            case R.id.item_info:

            {
                Intent intent = new Intent(mContext,ElementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MainConstant.CATEGORY_ID_EXTRA, dataModel.getIdCategory());
                intent.putExtra(MainConstant.CATEGORY_THUBMAIL_EXTRA, dataModel.getUrlCategory());
                intent.putExtra(MainConstant.CATEGORY_NAME_EXTRA, dataModel.getName());
                mContext.startActivity(intent);
            }
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Category dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name_category);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        Picasso.with(mContext).load(dataModel.getUrlCategory()).placeholder(R.drawable.logo).into(viewHolder.info);


        // Return the completed view to render on screen
        if(position == 0){
            convertView.getLayoutParams().height = (mRowHeight*40)/100;
        }else {
            int countElement =  dataSet.size()-1;
            convertView.getLayoutParams().height = ((mRowHeight*(60/countElement))/100)-5;
        }

        convertView.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        return convertView;
    }

}