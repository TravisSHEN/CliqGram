package cliq.com.cliqgram.model;

import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.helper.Utils;

/**
 * Created by Ilkan on 27/09/2015.
 */
public class Post {

    private String        postId;
    private byte[]        photoData;
    private String        description;
    private String        location;
    private List<Comment> commentList;
    private Date          createdAt;
    private List<Like>    likeList;
    private ParseUser     parseUser;

    public Post(byte[] photoData, String description, String location, ParseUser parseUser) {
        this.photoData = photoData;
        this.description = description;
        this.location = location;
        this.parseUser = parseUser;
        this.commentList = null;
        this.createdAt = new Date();
        this.likeList = null;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photo) {
        this.photoData = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public ParseUser getParseUser() {
        return parseUser;
    }

    public void setParseUser(ParseUser parseUser) {
        this.parseUser = parseUser;
    }
}
