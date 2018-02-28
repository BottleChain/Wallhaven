package com.njp.android.wallhaven.bean;

/**
 * 图片信息实体类
 */

public class ImageInfo {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    public String getLargeImgUrl() {
        return largeImgUrl;
    }

    public void setLargeImgUrl(String largeImgUrl) {
        this.largeImgUrl = largeImgUrl;
    }

    private String id;

    private String smallImgUrl;

    private String largeImgUrl;

}
