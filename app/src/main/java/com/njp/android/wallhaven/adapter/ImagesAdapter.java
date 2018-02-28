package com.njp.android.wallhaven.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.bean.ImageInfo;

import java.util.List;

/**
 * 图片展示列表适配器
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageInfo> mImageInfoList;
    private OnImageItemClickListener mListener;

    public void setListener(OnImageItemClickListener listener) {
        mListener = listener;
    }

    public ImagesAdapter(Context context, List<ImageInfo> imageInfoList) {
        mContext = context;
        mImageInfoList = imageInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ImageInfo imageInfo = mImageInfoList.get(position);
        Glide.with(mContext).load(imageInfo.getSmallImgUrl()).into(holder.image);
        if (mListener != null) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(imageInfo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewGroup item;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface OnImageItemClickListener {
        void onClick(ImageInfo imageInfo);
    }

}
