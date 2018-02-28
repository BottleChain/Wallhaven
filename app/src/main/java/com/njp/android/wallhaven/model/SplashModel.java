package com.njp.android.wallhaven.model;

import com.njp.android.wallhaven.bean.BingImageInfo;
import com.njp.android.wallhaven.utils.RetrofitUtil;

import io.reactivex.Observable;


/**
 * Splash页数据访问层
 */

public class SplashModel {

    public Observable<BingImageInfo> getImageInfo() {
        return RetrofitUtil.getBingImageService().getImage("js",0,1);
    }


}
