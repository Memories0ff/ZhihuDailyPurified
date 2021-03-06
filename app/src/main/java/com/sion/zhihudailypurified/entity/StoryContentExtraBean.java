package com.sion.zhihudailypurified.entity;

public class StoryContentExtraBean {

    /**
     * long_comments : 0
     * popularity : 65
     * short_comments : 18
     * comments : 18
     */

    private int long_comments;
    private int popularity;
    private int short_comments;
    private int comments;

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public StoryContentExtraBean(int long_comments, int popularity, int short_comments, int comments) {
        this.long_comments = long_comments;
        this.popularity = popularity;
        this.short_comments = short_comments;
        this.comments = comments;
    }
}
