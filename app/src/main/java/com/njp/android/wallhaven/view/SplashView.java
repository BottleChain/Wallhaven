package com.njp.android.wallhaven.view;

/**
 * Splash页面的视图层回调接口
 */

public interface SplashView extends BaseView {

    void onImageResponse(String url);

    void onImageError();

    void onTimer(int seconds);

    void onTimerFinish();

}
