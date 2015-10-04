package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import cliq.com.cliqgram.services.CommentService;
import cliq.com.cliqgram.services.UserService;

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
        ParseUser parseUser = this.getParseUser("owner");
        User owner = UserService.getUserFromParseUser(parseUser);
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
//                    AppStarter.eventBus.post(new ModelReadyToSave(Comment
//                            .this));
//                } else {
//                    Log.e("Comment", "User not found");
//                }
//            }
//        });
//
        ParseUser currentUser = ParseUser.getCurrentUser();
        this.put("owner", currentUser);
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
