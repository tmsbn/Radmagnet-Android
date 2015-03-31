package com.radmagnet.models;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tmsbn on 2/15/15.
 */
public class News extends RealmObject {

    private String category = "";


    @PrimaryKey
    private String id;


    private String postId = "";


    private Date createdDate;
    private Date startDate;

    private boolean active;
    private String headline = "";
    private String imageUrl = "";
    private String creator = "";
    private String creatorDp = "";
    private String location = "";
    private String shareUrl="";
    private long key = 0;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }



    private boolean isBookmarked = false;

    public Date getStartDate() {
        return startDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    @Ignore
    private String source;


    public String getPostId() {
        return postId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public String getHeadline() {
        return headline;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorDp() {
        return creatorDp;
    }

    public String getSource() {
        return source;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatorDp(String creatorDp) {
        this.creatorDp = creatorDp;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
