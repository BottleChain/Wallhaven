package com.njp.android.wallhaven.utils.glide;


import android.support.annotation.Nullable;

public interface LoadImageListener {

    void onStart();

    void onProgress(int progress);

    void onSuccess(@Nullable String path);

    void onFailure();
}
