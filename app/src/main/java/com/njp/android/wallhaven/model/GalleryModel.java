package com.njp.android.wallhaven.model;

import com.njp.android.wallhaven.utils.RetrofitUtil;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static com.njp.android.wallhaven.ui.GalleryFragment.TAG_LATEST;
import static com.njp.android.wallhaven.ui.GalleryFragment.TAG_RANDOM;
import static com.njp.android.wallhaven.ui.GalleryFragment.TAG_TOPLIST;

/**
 * 图片展示页面数据访问层
 */

public class GalleryModel {

    private int tag;

    public GalleryModel(int tag) {
        this.tag = tag;
    }

    public Observable<ResponseBody> getImages(int page) {
        String path = "";
        switch (tag) {
            case TAG_RANDOM:
                path = "random";
                break;
            case TAG_LATEST:
                path = "latest";
                break;
            case TAG_TOPLIST:
                path = "toplist";
                break;
        }
        return RetrofitUtil.getGalleryImageService().getImages(path, page);
    }

}
