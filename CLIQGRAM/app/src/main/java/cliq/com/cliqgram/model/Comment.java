package cliq.com.cliqgram.model;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class Comment {

    private String commentId;
    private String content;
    private Date createdAt;
    private ParseUser owner;
    private Post post;

    public Comment(String content, ParseUser owner, Post post){
        this.content = content;
        this.owner = owner;
        this.post = post;
    }
    public Comment(String content, ParseUser owner){
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

    public ParseUser getOwner() {
        return owner;
    }

    public void setOwner(ParseUser owner) {
        this.owner = owner;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
