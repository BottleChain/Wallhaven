package com.njp.android.wallhaven.utils.glide;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GlideUtil {

    /**
     * 普通的图片加载
     */
    public static void simpleLoad(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 普通的图片加载
     */
    public static void simpleLoad(Context context, int drawable, ImageView imageView) {
        Glide
                .with(context)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 带进度的图片加载
     *
     * @param context
     * @param url
     * @param imageView
     * @param listener
     */
    public static void progressLoad(Context context, String url, ImageView imageView, final LoadImageListener listener) {

        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(final int progress) {
                new Observable<Integer>() {
                    @Override
                    protected void subscribeActual(Observer<? super Integer> observer) {
                        observer.onNext(progress);
                    }
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                listener.onProgress(progress);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

        Glide
                .with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        listener.onStart();
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        listener.onSuccess(null);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        listener.onFailure();
                    }
                });

    }

    /**
     * 带进度的图片下载
     *
     * @param context
     * @param url
     * @param listener
     */
    public static void progressDownload(Context context, String url, final LoadImageListener listener) {

        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(final int progress) {
                new Observable<Integer>() {
                    @Override
                    protected void subscribeActual(Observer<? super Integer> observer) {
                        observer.onNext(progress);
                    }
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                listener.onProgress(progress);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

        Glide
                .with(context)
                .load(url)
                .downloadOnly(new DownloadTarget(listener));

    }
}
