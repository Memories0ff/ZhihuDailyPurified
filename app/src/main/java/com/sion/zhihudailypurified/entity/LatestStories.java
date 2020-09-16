package com.sion.zhihudailypurified.entity;

import java.util.List;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class LatestStories {

    /**
     * date : 20200405
     * stories : [{"image_hue":"0x38504f","title":"哪部电影中的景色，让你产生了「说走就走」的冲动？","url":"https://daily.zhihu.com/story/9722415","hint":"木易movie · 3 分钟阅读","ga_prefix":"040516","images":["https://pic3.zhimg.com/v2-da81313f016fc6727f9712a6f80c6736.jpg"],"type":0,"id":9722415},{"image_hue":"0x869166","title":"如何科学地进行老母猪产后心理健康指导？","url":"https://daily.zhihu.com/story/9722410","hint":"耗子领袖 · 2 分钟阅读","ga_prefix":"040511","images":["https://pic1.zhimg.com/v2-d53f266d52df83ed02c9cfabab1dd844.jpg"],"type":0,"id":9722410},{"image_hue":"0x89664b","title":"从小帮家长做家务的孩子长大以后怎么样了？","url":"https://daily.zhihu.com/story/9722404","hint":"常爸-黄任 · 3 分钟阅读","ga_prefix":"040509","images":["https://pic1.zhimg.com/v2-8ecb44915d042c7236ffc28601e3a408.jpg"],"type":0,"id":9722404},{"image_hue":"0x9e7b6f","title":"放轻松，深呼吸，我们开始麻醉了","url":"https://daily.zhihu.com/story/9722400","hint":"kkhenry · 6 分钟阅读","ga_prefix":"040507","images":["https://pic2.zhimg.com/v2-7a6b782745dd0252e1b72d0239199849.jpg"],"type":0,"id":9722400}]
     * top_stories : [{"image_hue":"0x7da1b3","hint":"作者 / 苏澄宇","url":"https://daily.zhihu.com/story/9722376","image":"https://pic1.zhimg.com/v2-c7028679b8a75ea89cf889f5df898b70.jpg","title":"科学家在南海首次发现鲸落，鲸落是什么？","ga_prefix":"040407","type":0,"id":9722376},{"image_hue":"0x806c59","hint":"作者 / 星球研究所","url":"https://daily.zhihu.com/story/9722200","image":"https://pic4.zhimg.com/v2-580ff8f99a4c743e7b54289f5989be6b.jpg","title":"中国最美的春天在哪里？","ga_prefix":"033009","type":0,"id":9722200},{"image_hue":"0x5f7887","hint":"作者 / 苏澄宇","url":"https://daily.zhihu.com/story/9722142","image":"https://pic2.zhimg.com/v2-a07c6b051e95336006c1c25379f5ff39.jpg","title":"鱼会不会尿尿呢？","ga_prefix":"032920","type":0,"id":9722142},{"image_hue":"0xb38f7d","hint":"作者 / 二手的科学家","url":"https://daily.zhihu.com/story/9722015","image":"https://pic1.zhimg.com/v2-bc4ddc52ccbc5a5077f44898b16ac5b8.jpg","title":"研制疫苗时，跳过动物试验直接进行人体试验有什么危险？","ga_prefix":"032516","type":0,"id":9722015},{"image_hue":"0xb39d7d","hint":"作者 / 大菠萝盖","url":"https://daily.zhihu.com/story/9722037","image":"https://pic2.zhimg.com/v2-1a433e2cdd71c12e4aac6ed7e4737f9d.jpg","title":"想自主创业短视频，我该从哪里入手？","ga_prefix":"032611","type":0,"id":9722037}]
     */

    private String date;
    private List<StoryBean> stories;
    private List<TopStoryBean> top_stories;

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

    public List<TopStoryBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoryBean> top_stories) {
        this.top_stories = top_stories;
    }

}
