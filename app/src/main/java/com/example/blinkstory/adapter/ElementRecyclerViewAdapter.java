package com.example.blinkstory.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.blinkstory.R;
import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.model.entity.Element;
import com.example.blinkstory.viewholder.ElementViewHolders;
import com.example.blinkstory.viewholder.LoadingHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

    public class ElementRecyclerViewAdapter extends RecyclerView.Adapter  {

        private List<Element> itemList;
        private Context context;
        private final int TYPE_VIDEO = 1;

    public ElementRecyclerViewAdapter(Context context,
            List<Element> itemList)
        {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            if(viewType== MainConstant.VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_item, parent, false);
                return new ElementViewHolders(view);
            } else if(viewType==MainConstant.VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_process, parent, false);
                return new LoadingHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            holder.setIsRecyclable(false);

            if(holder instanceof ElementViewHolders) {
                ElementViewHolders elementViewHolders=(ElementViewHolders)holder;
                elementViewHolders.elementImg.setTag(itemList.get(position));
                elementViewHolders.iconVideo.setVisibility(itemList.get(position) != null &&itemList.get(position).getTypeData() == TYPE_VIDEO ? View.VISIBLE : View.GONE);

                Picasso.with(context)
                        .load(itemList.get(position).getSrcThumbail())
                        .into(elementViewHolders.elementImg);
                elementViewHolders.bind(itemList.get(position));

            } else if(holder instanceof LoadingHolder) {
                LoadingHolder loadingHolder=(LoadingHolder)holder;
            }

            if(itemList==null) {
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
        public int getItemCount()
        {
            return itemList == null ? 0 : itemList.size();
        }
        @Override
        public int getItemViewType(int position) {
            return itemList.get(position) == null ? MainConstant.VIEW_TYPE_LOADING : MainConstant.VIEW_TYPE_ITEM;
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

    }