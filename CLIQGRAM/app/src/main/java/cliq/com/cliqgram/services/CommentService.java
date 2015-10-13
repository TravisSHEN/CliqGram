package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.CommentFailEvent;
import cliq.com.cliqgram.events.CommentSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 27/09/2015.
 */
public class CommentService {

    /**
     *
     * @param post
     * @param comment
     */
    public static void comment(Post post, final Comment comment){

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(post.getObjectId(), new GetCallback<Post>() {
            @Override
            public void done(Post postObject, ParseException e) {
                if(e == null){

                    List<Comment> relation = (ArrayList) postObject.get
                            ("comments");
                    if(relation == null){
                        relation = new ArrayList<>();
                    }
                    relation.add(comment);
                    postObject.put("comments", relation);
                    postObject.saveInBackground(new SaveCallback() {
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
                }else{
                    AppStarter.eventBus.post(new CommentFailEvent("Comment failed - " +
                            e.getMessage()));
                }
            }
        });
    }

}
