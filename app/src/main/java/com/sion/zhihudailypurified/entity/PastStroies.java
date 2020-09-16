package com.sion.zhihudailypurified.entity;

import java.util.List;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class PastStroies {

    /**
     * date : 20200403
     * stories : [{"image_hue":"0x2e2027","title":"NBA 球员的工资是以何种方式发放的？","url":"https://daily.zhihu.com/story/9722342","hint":"网易体育 · 5 分钟阅读","ga_prefix":"040320","images":["https://pic3.zhimg.com/v2-22f759f4be5cbf3219833b7fab0b582e.jpg"],"type":0,"id":9722342},{"image_hue":"0x453d30","title":"未来线上观影会不会和院线观影齐头并进?","url":"https://daily.zhihu.com/story/9722345","hint":"张小北 · 2 分钟阅读","ga_prefix":"040316","images":["https://pic3.zhimg.com/v2-0b665f541fcdf93695f66f72814ec1da.jpg"],"type":0,"id":9722345},{"image_hue":"0x33242d","title":"如何看待 cosplay 文化？","url":"https://daily.zhihu.com/story/9722346","hint":"艾菲趣日本 · 13 分钟阅读","ga_prefix":"040311","images":["https://pic2.zhimg.com/v2-76e4a87dd74a508fb7be8fe7ac54edc5.jpg"],"type":0,"id":9722346},{"image_hue":"0x151f18","title":"哪部电影中的景色，让你产生了「说走就走」的冲动？","url":"https://daily.zhihu.com/story/9722355","hint":"淘气小企鹅 · 2 分钟阅读","ga_prefix":"040309","images":["https://pic2.zhimg.com/v2-2238505c8b4eae4f387c12fc68357dd9.jpg"],"type":0,"id":9722355},{"image_hue":"0x9c826d","title":"为什么中国车企设计不出「可爱」的车型？","url":"https://daily.zhihu.com/story/9722361","hint":"王洪浩 · 3 分钟阅读","ga_prefix":"040307","images":["https://pic1.zhimg.com/v2-927142d08061da808155045b256e3e90.jpg"],"type":0,"id":9722361},{"image_hue":"0xb39f7d","title":"瞎扯 · 如何正确地吐槽","url":"https://daily.zhihu.com/story/9722329","hint":"VOL.2367","ga_prefix":"040306","images":["https://pic4.zhimg.com/v2-4c32f9f7b0a28f0d604f5f432dd4be1b.jpg"],"type":0,"id":9722329}]
     */

    private String date;
    private List<StoryBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoryBean> getStories() {
        return stories;
    }

    public void setStories(List<StoryBean> stories) {
        this.stories = stories;
    }

}