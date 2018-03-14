package com.njp.android.wallhaven.utils;

import android.content.Context;

import com.njp.android.wallhaven.bean.DaoMaster;
import com.njp.android.wallhaven.bean.DaoSession;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.bean.ImageInfoDao;

import java.io.File;
import java.util.List;


/**
 * 图片信息数据访问对象
 */

public class ImageDao {

    private static DaoSession sSession;

    public static void init(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "image_info.db");
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDb());
        sSession = daoMaster.newSession();
    }

    public static List<ImageInfo> queryAll() {
        ImageInfoDao dao = sSession.getImageInfoDao();
        return dao.loadAll();
    }

    public static void insert(ImageInfo imageInfo) {
        ImageInfoDao dao = sSession.getImageInfoDao();
        dao.insert(imageInfo);
    }

    public static void delete(ImageInfo imageInfo) {
        ImageInfoDao dao = sSession.getImageInfoDao();
        dao.deleteByKey(imageInfo.getId());
    }

    public static boolean exists(ImageInfo imageInfo) {
        ImageInfoDao dao = sSession.getImageInfoDao();
        return dao.load(imageInfo.getId()) != null;
    }

}
