package com.sion.zhihudailypurified.entity;

import kotlinx.android.parcel.Parcelize;

public class TopStoryBean {
    /**
     * image_hue : 0x7da1b3
     * hint : 作者 / 苏澄宇
     * url : https://daily.zhihu.com/story/9722376
     * image : https://pic1.zhimg.com/v2-c7028679b8a75ea89cf889f5df898b70.jpg
     * title : 科学家在南海首次发现鲸落，鲸落是什么？
     * ga_prefix : 040407
     * type : 0
     * id : 9722376
     */

    private String image_hue;
    private String hint;
    private String url;
    private String image;
    private String title;
    private String ga_prefix;
    private int type;
    private int id;

    public String getImage_hue() {
        return image_hue;
    }

    public void setImage_hue(String image_hue) {
        this.image_hue = image_hue;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}