package com.njp.android.wallhaven.model;

import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.RetrofitUtil;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 搜索界面数据访问层
 */

public class SearchModel {

    public Observable<ResponseBody> search(String q, String sorting, int page) {
        return RetrofitUtil.getGalleryImageService().searchImages(q, sorting, page);
    }

}
