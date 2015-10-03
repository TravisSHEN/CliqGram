package cliq.com.cliqgram.model;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.CommentReadyEvent;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.UserService;
import de.greenrobot.event.Subscribe;

/**
 * Created by litaoshen on 22/09/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject implements Comparable<Comment>{


    public static Comment createComment(User owner, Post post, String content) {

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setOwner(owner);
        return comment;
    }

    public Comment(){
        super();

        AppStarter.eventBus.register(this);
    }

    @Subscribe
    public void onReadySaveEvent(ModelReadyToSave event){
        // save comment to Parse
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if( e == null ){
                    AppStarter.eventBus.post(new CommentReadyEvent(Comment
                            .this));
                } else {
                    AppStarter.eventBus.post(new CommentReadyEvent(e.getMessage()));
                }
            }
        });
    }

    public String getContent() {
        return this.getString("content");
    }

    public void setContent(String content) {
        this.put("content", content);
    }

    public User getOwner(){
        ParseUser parseUser = this.getParseUser("owner");
        User owner = UserService.getUserFromParseUser(parseUser);
        return owner;
    }

    public void setOwner(User owner) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", owner.getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    Comment.this.put("owner", objects.get(0));
                    AppStarter.eventBus.post(new ModelReadyToSave());
                } else {
                    Log.e("Comment", "User not found");
                }
            }
        });


//        this.put("owner", UserService.findParseUserByName(owner.getUsername()));
    }

    public Post getPost() {
        Post post = (Post) this.getParseObject("post");
        return post;

    }

    public void setPost(Post post){
        this.put("post", post);
    }

    @Override
    public int compareTo(Comment another) {
        return -1 * this.getCreatedAt().compareTo(another.getCreatedAt());
    }

    private static class ModelReadyToSave extends BaseEvent {

        public ModelReadyToSave(){
            super("Post model ready");
        }
        public ModelReadyToSave(String message) {
            super(message);
        }
    }

}
