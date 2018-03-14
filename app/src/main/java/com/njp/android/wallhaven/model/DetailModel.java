package com.njp.android.wallhaven.model;

import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.RetrofitUtil;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 详情界面的数据访问层
 */

public class DetailModel {

    public Observable<ResponseBody> getDetailImage(String id) {
        return RetrofitUtil.getGalleryImageService().getDetailImage(id);
    }

}
