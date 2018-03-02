package com.njp.android.wallhaven.view;

import com.njp.android.wallhaven.bean.DetailImageInfo;

/**
 * 详情界面视图层回调接口
 */

public interface DetailView extends BaseView {

    void onSuccess(DetailImageInfo imageInfo);

    void onError();

}
