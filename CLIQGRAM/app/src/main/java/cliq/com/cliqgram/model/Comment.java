package cliq.com.cliqgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class Comment implements Parcelable {

    private String commentId;
    private String content;
    private Date createdAt;
    private User owner;
    private String postId;
    private Post post;

    public Comment(){

    }

    public static Comment createComment(User owner, Post post, String content) {

        Comment comment = new Comment(content, owner, post);

        return comment;
    }

    public Comment(String content, User owner, Post post){
        this.content = content;
        this.owner = owner;
        this.post = post;
    }
    public Comment(String content, User owner){
        this.content = content;
        this.owner = owner;
    }
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Comment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commentId);
        dest.writeString(this.content);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeParcelable(this.owner, 0);
        dest.writeParcelable(this.post, 0);
    }

    private Comment(Parcel in) {
        this.commentId = in.readString();
        this.content = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.post = in.readParcelable(Post.class.getClassLoader());
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
