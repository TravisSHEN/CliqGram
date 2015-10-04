package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cliq.com.cliqgram.events.CommentFailEvent;
import cliq.com.cliqgram.events.LikeSuccessEvent;
import cliq.com.cliqgram.model.Like;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class LikeService {

    /**
     *
     * @param post
     * @param like
     */
    public static void like(Post post, final Like like){

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(post.getObjectId(), new GetCallback<Post>() {
            @Override
            public void done(Post postObject, ParseException e) {
                if(e == null){

                    List<Like> relation = (ArrayList) postObject.get
                            ("likes");
                    if(relation == null){
                        relation = new ArrayList<>();
                    }
                    relation.add(like);
                    postObject.put("likes", relation);
                    postObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                AppStarter.eventBus.post(new
                                        LikeSuccessEvent(like));
                            } else {
                                AppStarter.eventBus.post(new LikeSuccessEvent
                                        ("Like failed - " +
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
    public static void unLike(Post post, final Like like){

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(post.getObjectId(), new GetCallback<Post>() {
            @Override
            public void done(Post postObject, ParseException e) {
                if(e == null){

                    List<Like> relation = (ArrayList) postObject.get
                            ("likes");
                    if(relation == null){
                        relation = new ArrayList<>();
                    }
                    relation.remove(like);
                    postObject.put("likes", relation);
                    postObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                AppStarter.eventBus.post(new
                                        LikeSuccessEvent(like));
                            } else {
                                AppStarter.eventBus.post(new LikeSuccessEvent
                                        ("Like failed - " +
                                                e.getMessage()));
                            }
                        }
                    });

                    like.deleteInBackground();
                }else{
                    AppStarter.eventBus.post(new CommentFailEvent("Comment failed - " +
                            e.getMessage()));
                }
            }
        });
    }

    public static boolean isAlreadyLiked(ParseUser parseUser, List<Like> likeList){

        boolean isLiked = false;

        if (likeList == null || likeList.isEmpty()){
           return isLiked;
        }

        for(Iterator<Like> iter = likeList.listIterator(); iter.hasNext();){
            Like like = iter.next();
            if( like.getUser().getUsername().equals(parseUser.getUsername())){
                isLiked = true;
                return  isLiked;
            }
        }

        return isLiked;

    }
}
