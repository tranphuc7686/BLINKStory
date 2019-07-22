package com.example.blinkstory.viewholder;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.blinkstory.R;
import com.example.blinkstory.ViewImageElementActivity;
import com.example.blinkstory.ViewVideoElementActivity;
import com.example.blinkstory.model.entity.Element;

public class ElementViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView elementImg;
    public ImageView iconVideo;
    public Element element;
    private final int TYPE_VIDEO = 1;
    public ElementViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        elementImg = (ImageView) itemView.findViewById(R.id.element_image);
        iconVideo = (ImageView) itemView.findViewById(R.id.icon_video_element);
    }

    @Override
    public void onClick(View view)
    {
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
    public void bind(@NonNull Element element) {
       this.element = element;
    }
}