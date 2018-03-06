package com.njp.android.wallhaven.utils.glide;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.io.File;


public class DownloadTarget implements Target<File> {

    private LoadImageListener mListener;

    public DownloadTarget(LoadImageListener listener) {
        mListener = listener;
    }


    @Override
    public void onLoadStarted(Drawable placeholder) {
        mListener.onStart();
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        mListener.onFailure();
    }

    @Override
    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
        mListener.onSuccess(resource.getPath());
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {

    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public void setRequest(Request request) {

    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
