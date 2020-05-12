package com.sion.zhihudailypurified.entity;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sion.zhihudailypurified.db.ListJsonConverter;

import java.util.List;

@Entity(indices = {@Index(value = {"date"})})
@TypeConverters(ListJsonConverter.class)
public class StoryBean {
    /**
     * image_hue : 0x2e2027
     * title : NBA 球员的工资是以何种方式发放的？
     * url : https://daily.zhihu.com/story/9722342
     * hint : 网易体育 · 5 分钟阅读
     * ga_prefix : 040320
     * images : ["https://pic3.zhimg.com/v2-22f759f4be5cbf3219833b7fab0b582e.jpg"]
     * type : 0
     * id : 9722342
     */

    @PrimaryKey
    private int id;
    private String image_hue;
    private String title;
    private String url;
    private String hint;
    private String ga_prefix;
    private int type;
    private List<String> images;
    private String date;
    //是否已读
    @Ignore
    private ObservableField<Boolean> isRead = new ObservableField<>();
    //同一天story加载顺序（仿照json数据的顺序加载）
    private int loadingOrder;

    public String getImage_hue() {
        return image_hue;
    }

    public void setImage_hue(String image_hue) {
        this.image_hue = image_hue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ObservableField<Boolean> getIsRead() {
        if (isRead.get() == null) {
            isRead.set(false);
        }
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead.set(isRead);
    }

    public int getLoadingOrder() {
        return loadingOrder;
    }

    public void setLoadingOrder(int loadingOrder) {
        this.loadingOrder = loadingOrder;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
