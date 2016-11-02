package com.xperfect.tt.lib.model;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Kyle on 2016/10/13.
 */
public class ImageBean implements Serializable {

    public String imageName = "";
    public String imagePath = "";
    public Long createTime = 0L;

    public boolean isSelected = false;

    public ImageBean() {
    }

    public ImageBean(String imageName, String imagePath, Long createTime) {
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString( this );
    }
}