package com.example.blinkstory.elements;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.blinkstory.Constant;
import com.example.blinkstory.R;
import com.example.blinkstory.model.entity.Element;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ElementsAdapter extends RecyclerView.Adapter {

    private List<Element> itemList;
    private Context context;
    private final int TYPE_VIDEO = 1;
    private OnItemClickListener mOnItemClickListener;

    public ElementsAdapter(Context context,
                           List<Element> itemList, OnItemClickListener mOnItemClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_item, parent, false);
            final ElementViewHolders elementViewHolders = new ElementViewHolders(view);
            elementViewHolders.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(itemList.get(elementViewHolders.getAdapterPosition()));
                }
            });
            return elementViewHolders;
        } else if (viewType == Constant.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_process, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        if (holder instanceof ElementViewHolders) {
            ElementViewHolders elementViewHolders = (ElementViewHolders) holder;
            elementViewHolders.elementImg.setTag(itemList.get(position));

            elementViewHolders.iconVideo.setVisibility(itemList.get(position) != null && itemList.get(position).getTypeData() == TYPE_VIDEO ? View.VISIBLE : View.GONE);

            Picasso.with(context)
                    .load(itemList.get(position).getSrcThumbail())
                    .placeholder(R.mipmap.ic_launcher_blink)
                    .into(elementViewHolders.elementImg);


        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
        }

        if (itemList == null) {
            // Span the item if active
            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(true);
                holder.itemView.setLayoutParams(sglp);
            }
        } else {
            // Span the item if active
            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(false);
                holder.itemView.setLayoutParams(sglp);
            }
        }
    }


    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) == null ? Constant.VIEW_TYPE_LOADING : Constant.VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addLoadingView() {
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                itemList.add(null);
                notifyItemInserted(itemList.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        //Remove loading item
        itemList.remove(itemList.size() - 1);
        notifyItemRemoved(itemList.size());
    }

    public void addData(List<Element> dataViews) {
        this.itemList.addAll(dataViews);
        notifyDataSetChanged();

    }

    public interface OnItemClickListener {
        void onItemClick(Element element);
    }

    public class ElementViewHolders extends RecyclerView.ViewHolder {

        public ImageView elementImg;
        public ImageView iconVideo;
        public Element element;
        private InterstitialAd mInterstitialAd;
        public View container;

        public ElementViewHolders(View itemView) {
            super(itemView);
            container = itemView;
            elementImg = (ImageView) itemView.findViewById(R.id.element_image);
            iconVideo = (ImageView) itemView.findViewById(R.id.icon_video_element);
        }
    }
    public class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }


}