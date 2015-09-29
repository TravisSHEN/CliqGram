package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cliq.com.cliqgram.events.CommentFailEvent;
import cliq.com.cliqgram.events.CommentSuccessEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 27/09/2015.
 */
public class CommentService {

    public static void comment(final ParseObject postObject, Comment comment){


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.getInBackground(postObject.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null){
                    Comment comment = new Comment("content", ParseUser.getCurrentUser());
                    ParseObject parseObject = new ParseObject("Comment");
                    parseObject.put("owner", comment.getOwner());
                    parseObject.put("content", comment.getContent());
                    parseObject.saveInBackground();
                    ArrayList<ParseObject> relation = (ArrayList)postObject.get("comments");
                    if(relation == null){
                        relation = new ArrayList<ParseObject>();
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
