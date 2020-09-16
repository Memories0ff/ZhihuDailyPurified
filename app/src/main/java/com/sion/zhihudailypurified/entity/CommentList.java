package com.sion.zhihudailypurified.entity;

import java.util.List;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class CommentList {

    private List<CommentBean> comments;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }


}
