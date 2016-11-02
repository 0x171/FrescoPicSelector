package com.xperfect.tt.lib.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kyle on 2016/10/13.
 */
public class FolderBean implements Serializable {

    public String name = "";
    public String path = "";
    public ImageBean cover = new ImageBean();
    public ArrayList<ImageBean> images = new ArrayList<>();

    public boolean isSelected = false;

    public FolderBean() {
    }

    public FolderBean(String name, String path, ImageBean cover, ArrayList<ImageBean> images) {
        this.name = name;
        this.path = path;
        this.cover = cover;
        this.images = images;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}