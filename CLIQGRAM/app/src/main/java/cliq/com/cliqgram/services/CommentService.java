package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import cliq.com.cliqgram.events.CommentFailEvent;
import cliq.com.cliqgram.events.CommentSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 27/09/2015.
 */
public class CommentService {

    public static final String TABLE_NAME = "Comment";


    public static void comment(Post post, final Comment comment){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.getInBackground(post.getPostId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject postObject, ParseException e) {
                if(e == null){
                    ParseUser currentUser = ParseUser.getCurrentUser();

                    ParseObject parseObject = new ParseObject(TABLE_NAME);
                    parseObject.put("owner", currentUser);
                    parseObject.put("content", comment.getContent());
                    parseObject.put("post", postObject);
                    parseObject.saveInBackground();
                    ArrayList<ParseObject> relation = (ArrayList)postObject.get("comments");
                    if(relation == null){
                        relation = new ArrayList<>();
                    }
                    relation.add(parseObject);
                    postObject.put("comments", relation);
                    postObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                AppStarter.eventBus.post(new CommentSuccessEvent());
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
