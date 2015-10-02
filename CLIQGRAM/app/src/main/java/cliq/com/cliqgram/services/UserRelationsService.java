package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.model.User;

/**
 * Created by ilkan on 2/10/2015.
 */
public class UserRelationsService {

    public static void follow(final String userName){

        final ParseUser currentUser = ParseUser.getCurrentUser();
        final ParseUser otherUser = UserService.findParseUserByName(userName);
        followGenericAction(userName, "followers", currentUser);
        followGenericAction(currentUser.getUsername(), "followings", otherUser);
    }

    private static void followGenericAction(final String userName, final String operationName, final ParseUser user){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserRelations");
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    ParseObject userRelations;
                    if(objects== null || objects.size() == 0){
                        userRelations = new ParseObject("UserRelations");
                        userRelations.put("username", userName);
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
                    //TODO event
                }
            }
        });

    }

    public static List<ParseUser> getRelation(String userName, String relation){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserRelations");
        query.whereEqualTo("username", userName);
        List<ParseUser> userRelations = null;
        try {
            List<ParseObject> parseObjects = (List<ParseObject>)query.find();
            if(parseObjects != null && parseObjects.size() == 1){
                userRelations = (List<ParseUser>)parseObjects.get(0).get(relation);
            }
        }catch(ParseException e){
            //TODO exception
        }finally {
            return userRelations;
        }

    }


}
