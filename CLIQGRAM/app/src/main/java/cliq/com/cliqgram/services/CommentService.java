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
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.Params;
import cliq.com.cliqgram.utils.ParseObject2ModelConverter;

/**
 * Created by ilkan on 27/09/2015.
 */
public class CommentService {

    public static void comment(Post post, Comment comment){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Params.TABLE_POST);
        query.getInBackground(post.getPostId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject postObject, ParseException e) {
                if(e == null){
                    User currentUser = UserService.getCurrentUser();
                    Comment comment = new Comment(Params.FIELD_CONTENT, currentUser);

                    ParseObject commentObject = new ParseObject(Params.TABLE_COMMENT);
                    commentObject.put(Params.FIELD_OWNER, comment.getOwner());
                    commentObject.put(Params.FIELD_CONTENT, comment.getContent());
                    commentObject.put(Params.FIELD_POST_ID, postObject.get(Params.FIELD_OBJECT_ID));
                    commentObject.saveInBackground();
                    ArrayList<ParseObject> comments = (ArrayList)postObject.get(Params.FIELD_COMMENTS);
                    if(comments == null){
                        comments = new ArrayList<>();
                    }
                    comments.add(commentObject);

                    //comment event should be pushed as an activity as well
                    ParseObject activityObject = new ParseObject(Params.TABLE_ACTIVITY);
                    activityObject.put(Params.FIELD_ACTIVITY_TYPE, Params.ACTIVITY_COMMENT);
                    activityObject.put(Params.FIELD_USER, ParseUser.getCurrentUser());
                    activityObject.put(Params.FIELD_EVENT_ID, commentObject.getObjectId());

                    ArrayList<ParseObject> activities = (ArrayList)ParseUser.getCurrentUser().get(Params.FIELD_ACTIVITIES);
                    if(activities == null){
                        activities = new ArrayList<>();
                    }
                    activities.add(activityObject);

                    postObject.put(Params.FIELD_COMMENTS, comments);
                    postObject.saveInBackground();

                    ParseUser.getCurrentUser().put(Params.FIELD_ACTIVITIES, activities);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
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
    public void getCommentsForPost(String postId){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Params.TABLE_POST);
        query.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject postObject, ParseException e) {
                if(e == null && postObject != null){
                    ArrayList<ParseObject> comObjList = (ArrayList)postObject.get(Params.FIELD_COMMENTS);
                    if(comObjList != null){
                        ArrayList<Comment> commentList = new ArrayList<Comment>();
                        for (ParseObject comObj : comObjList) {
                            commentList.add(ParseObject2ModelConverter.toComment(comObj));
                        }
                        AppStarter.eventBus.post(new CommentSuccessEvent(commentList));
                    }
                }
            }
        });
    }
}
