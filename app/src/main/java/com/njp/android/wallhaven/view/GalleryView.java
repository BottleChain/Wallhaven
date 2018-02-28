package com.njp.android.wallhaven.view;

import com.njp.android.wallhaven.bean.ImageInfo;

import java.util.List;

/**
 * 图片展示页面视图层回调接口
 */

public interface GalleryView extends BaseView {

    void onRefreshSuccess(List<ImageInfo> images);

    void onRefreshError();

    void onLoadMoreSuccess(List<ImageInfo> images);

    void onLoadMoreError();

    void onNoMore();

}
