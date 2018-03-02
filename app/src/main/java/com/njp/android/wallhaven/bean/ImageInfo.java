package com.njp.android.wallhaven.bean;

import java.io.Serializable;

/**
 * 图片信息实体类
 */

public class ImageInfo implements Serializable {

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    private String smallImgUrl;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
