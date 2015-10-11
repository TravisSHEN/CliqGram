package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import cliq.com.cliqgram.services.CommentService;

/**
 * Created by litaoshen on 22/09/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject implements Comparable<Comment> {

    public static Comment createComment(User owner, Post post, String content) {

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setOwner(owner);

        comment.saveInBackground();
        CommentService.comment(post, comment);
        Activity.createActivity(owner, "comment", post.getObjectId());
        return comment;
    }

    public Comment() {
        super();

    }


    public String getContent() {
        return this.getString("content");
    }

    public void setContent(String content) {
        this.put("content", content);
    }

    public User getOwner() {
        User owner = (User) this.getParseUser("owner");
        return owner;
    }

    public void setOwner(User owner) {
        this.put("owner", owner);
    }

    public Post getPost() {
        Post post = (Post) this.getParseObject("post");
        return post;

    }

    public void setPost(Post post) {
        this.put("post", post);
    }

    @Override
    public int compareTo(Comment another) {
        return -1 * this.getCreatedAt().compareTo(another.getCreatedAt());
    }

}
