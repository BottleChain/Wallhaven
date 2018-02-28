package com.njp.android.wallhaven.view;

import com.njp.android.wallhaven.bean.ImageInfo;

import java.util.List;

/**
 * 搜索界面视图层回调接口
 */

public interface SearchView extends BaseView {

    void onSearchSuccess(List<ImageInfo> images);

    void onSearchError();

    void onSearchNone();

    void onLoadMoreSuccess(List<ImageInfo> images);

    void onLoadMoreError();

    void onNoMore();

}
