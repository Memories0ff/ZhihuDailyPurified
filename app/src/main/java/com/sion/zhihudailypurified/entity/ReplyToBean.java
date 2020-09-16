package com.sion.zhihudailypurified.entity;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class ReplyToBean {
    /**
     * content : 看到后羿说我教训我男朋友的时候
     * 我就敏锐地回头仔细看了下法师的话
     * 嗯？感觉怎么好像有点问题
     * 果然
     * 我真是厉害
     * status : 0
     * id : 33594859
     * author : 夭夭夭夭夭阿灼
     */

    private String content;
    private int status;
    private int id;
    private String author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}