package cliq.com.cliqgram.services;

import android.os.Handler;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.FollowersSuccessEvent;
import cliq.com.cliqgram.events.FollowingsSuccessEvent;
import cliq.com.cliqgram.events.UserSuccessEvent;
import cliq.com.cliqgram.helper.ProgressSpinner;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.Params;
import de.greenrobot.event.Subscribe;

/**
 * Created by ilkan on 2/10/2015.
 */
public class UserRelationsService {

    ParseUser otherUser;
    String userName;
    ParseUser currentUser;

    public void follow(String userName){

        this.userName = userName;
        this.currentUser = ParseUser.getCurrentUser();
        UserService.findParseUserByName(userName);

    }

    public void unfollow(String userName){
        //TODO should be implemented
    }

    @Subscribe
    public void onEvent(final BaseEvent baseEvent) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressSpinner.getInstance().dismissSpinner();
                if (baseEvent instanceof UserSuccessEvent) {
                    otherUser = ((UserSuccessEvent) baseEvent).getParseUser();
                    followGenericAction(userName, Params.FIELD_FOLLOWERS, currentUser);
                    followGenericAction(currentUser.getUsername(), Params.FIELD_FOLLOWINGS, otherUser);
                }
            }
        }, 2000);
    }

    private static void followGenericAction(final String userName, final String operationName, final ParseUser user){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Params.TABLE_USER_RELATIONS);
        query.whereEqualTo(Params.FIELD_USER_NAME, userName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    ParseObject userRelations;
                    if(objects== null || objects.size() == 0){
                        userRelations = new ParseObject(Params.TABLE_USER_RELATIONS);
                        userRelations.put(Params.FIELD_USER_NAME, userName);
                    }else{
                        userRelations = objects.get(0);
                    }

                    ArrayList<ParseUser> relation = (ArrayList<ParseUser>)userRelations.get(operationName);
                    if (relation == null) {
                        relation = new ArrayList<>();
                    }
                    relation.add(user);
                    userRelations.put(operationName, relation);
                    userRelations.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                //TODO event
                            }else{
                                //TODO event
                            }
                        }
                    });
                }else{
                    //TODO fail event
                }
            }
        });

    }

    public static void getRelation(String userName, final String relation){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Params.TABLE_USER_RELATIONS);
        query.whereEqualTo(Params.FIELD_USER_NAME, userName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null && objects.size() == 1) {
                    List<ParseUser> userRelations = (List<ParseUser>) objects.get(0).get(relation);
                    if (relation.equals(Params.FIELD_FOLLOWINGS)) {
                        AppStarter.eventBus.post(new FollowingsSuccessEvent(userRelations));
                    } else {
                        AppStarter.eventBus.post(new FollowersSuccessEvent(userRelations));
                    }

                } else {
                    //TODO fail event
                }
            }
        });

    }


}
