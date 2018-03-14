package com.njp.android.wallhaven.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.glide.GlideUtil;

import java.util.List;

/**
 * 图片展示列表适配器
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageInfo> mImageInfoList;
    private OnImageItemClickListener mClickListener;
    private OnImageItemLongClickListener mLongClickListener;

    public void setClickListener(OnImageItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setLongClickListener(OnImageItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
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
        GlideUtil.simpleLoad(mContext, imageInfo.getSmallImgUrl(), holder.image);
        if (mClickListener != null) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClick(imageInfo);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onLongClick(imageInfo, ImageDao.exists(imageInfo));
                    return false;
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

    public interface OnImageItemLongClickListener {
        void onLongClick(ImageInfo imageInfo, boolean exists);
    }

}
