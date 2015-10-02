package cliq.com.cliqgram.model;

import java.util.Date;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class Comment {

    private String commentId;
    private String content;
    private Date createdAt;
    private User owner;
    private Post post;

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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
