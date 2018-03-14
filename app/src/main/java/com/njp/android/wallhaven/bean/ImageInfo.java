package com.njp.android.wallhaven.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 图片信息实体类
 */
@Entity(nameInDb = "image_info.db")
public class ImageInfo implements Serializable {

    static final long serialVersionUID = 42L;


    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    @Property
    private String smallImgUrl;

    @Id
    private String id;


    @Generated(hash = 1091831279)
    public ImageInfo(String smallImgUrl, String id) {
        this.smallImgUrl = smallImgUrl;
        this.id = id;
    }

    @Generated(hash = 2139894022)
    public ImageInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
