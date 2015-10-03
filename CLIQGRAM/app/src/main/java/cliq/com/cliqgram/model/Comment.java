package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

import cliq.com.cliqgram.services.UserService;

/**
 * Created by litaoshen on 22/09/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject{

    private String commentId;
    private String content;
    private Date createdAt;
    private User owner;
    private Post post;

    public static Comment createComment(User owner, Post post, String content) {

        Comment comment = new Comment();
        comment.setOwner(owner);
        comment.setPost(post);
        comment.setContent(content);

        return comment;
    }

    public Comment(){
        super();
    }

    public String getCommentId() {
        commentId = this.getObjectId();
        return commentId;
    }

    public void setCommentId(String commentId) {
        if( this.getObjectId() != null ) {
            this.put("commentId", this.getObjectId());
        }
        this.commentId = this.getObjectId();
    }

    public Date getCreatedAt() {
        createdAt = this.getDate("createdAt");
        return createdAt;
    }

//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }

    public String getContent() {
        content = this.getString("content");
        return content;
    }

    public void setContent(String content) {
        this.put("content", content);
        this.content = content;
    }

    public User getOwner() {
        owner = UserService.getUserFromParseUser((ParseUser) this.get("owner"));
        return owner;
    }

    public void setOwner(User owner) {
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", owner.getUsername());
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if (e == null && objects.size() > 0) {
//                    Comment.this.put("owner", objects.get(0));
//                } else {
//                    Log.e("Comment", "User not found");
//                }
//            }
//        });

        this.put("owner", UserService.findParseUserByName(owner.getUsername()));
        this.owner = owner;
    }

    public Post getPost() {
        post = (Post) this.getParseObject("post");
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
