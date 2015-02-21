package tms.ubrats;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tmsbn on 2/15/15.
 */
public class News extends RealmObject {

    private String postType;

    @PrimaryKey
    private String postId;

    private Date createdDate;

    private boolean active;
    private String headline;
    private String imageUrl;
    private String creator;
    private String creatorDp;
    private boolean isBookmarked=false;

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    @Ignore
    private String source;

    public String getPostType() {
        return postType;
    }

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

    public void setPostType(String postType) {
        this.postType = postType;
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
