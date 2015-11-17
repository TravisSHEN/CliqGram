package cliq.com.cliqgram.services;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.CommentFailEvent;
import cliq.com.cliqgram.events.CommentSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.app.AppStarter;

/**
 * Created by ilkan on 27/09/2015.
 */
public class CommentService {

    /**
     * @param post
     * @param comment
     */
    public static void comment(Post post, final Comment comment) {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("comments");

        List<Comment> relation = (ArrayList) post.get
                ("comments");
        if (relation == null) {
            relation = new ArrayList<>();
        }
        relation.add(comment);
        post.put("comments", relation);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new
                            CommentSuccessEvent(comment));
                } else {
                    AppStarter.eventBus.post(new CommentFailEvent("Comment failed - " +
                            e.getMessage()));
                }
            }
        });
        Log.e("---- Comment ----", post.getObjectId());
    }

}
