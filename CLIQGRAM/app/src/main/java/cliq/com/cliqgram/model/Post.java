package cliq.com.cliqgram.model;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ilkan on 27/09/2015.
 */
public class Post {

    private String postId;
    private byte[] photoData;
    private String text;
    private String location;
    private List<Comment> commentList;
    private String createdAt;
    private List<Like> likeList;
    private ParseUser parseUser;

    public Post(byte[] photoData, String text, String location,
                ParseUser parseUser, List<Comment> commentList, String createdAt, List<Like> likeList){
        this.photoData = photoData;
        this.text = text;
        this.location = location;
        this.parseUser = parseUser;
        this.commentList = commentList;
        this.createdAt = createdAt;
        this.likeList = likeList;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
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
