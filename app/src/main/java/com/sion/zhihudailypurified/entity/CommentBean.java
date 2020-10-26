package com.sion.zhihudailypurified.entity;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

public class CommentBean {

    /**
     * own : false
     * author : 请叫我小胖咂
     * content : 最精彩的就是那条了
     * avatar : http://pic3.zhimg.com/6c8df7e7d23ecfc39349730662509c0a_im.jpg
     * time : 1588036001
     * reply_to : {"content":"看到后羿说我教训我男朋友的时候\n我就敏锐地回头仔细看了下法师的话\n嗯？感觉怎么好像有点问题\n果然\n我真是厉害","status":0,"id":33594859,"author":"夭夭夭夭夭阿灼"}
     * voted : false
     * id : 33601305
     * likes : 0
     */
    private boolean own;
    private String author;
    private String content;
    private String avatar;
    private long time;
    private ReplyToBean reply_to;
    private boolean voted;
    private int id;
    private int likes;

    //UI设置用
    private boolean isResignedForLineCount = false;
    private int quoteRealLineCount; //仅赋值一次
    private ObservableField<Boolean> isExpanded = new ObservableField<>(false);
    private ObservableField<Boolean> isExpandable = new ObservableField<>(false);

    private boolean isFirstLongComment = false; //第一条长评
    private boolean isFirstShortComment = false;//第一条短评


    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ReplyToBean getReply_to() {
        return reply_to;
    }

    public void setReply_to(ReplyToBean reply_to) {
        this.reply_to = reply_to;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getQuoteRealLineCount() {
        return quoteRealLineCount;
    }

    public void setQuoteRealLineCount(int quoteRealLineCount) {
        //仅赋值一次
        if (!isResignedForLineCount) {
            this.quoteRealLineCount = quoteRealLineCount;
            isResignedForLineCount = true;
        }
    }

    public ObservableField<Boolean> getIsExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(ObservableField<Boolean> isExpanded) {
        this.isExpanded = isExpanded;
    }

    public ObservableField<Boolean> getIsExpandable() {
        return isExpandable;
    }

    public void setIsExpandable(ObservableField<Boolean> isExpandable) {
        this.isExpandable = isExpandable;
    }

    public boolean isFirstLongComment() {
        return isFirstLongComment;
    }

    public void setFirstLongComment(boolean firstLongComment) {
        isFirstLongComment = firstLongComment;
    }

    public boolean isFirstShortComment() {
        return isFirstShortComment;
    }

    public void setFirstShortComment(boolean firstShortComment) {
        isFirstShortComment = firstShortComment;
    }

    //只有添加该项的操作，不需要严格判断相同
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }


}