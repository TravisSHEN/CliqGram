package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.UserRelation;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class RelationGetEvent extends BaseEvent{

    UserRelation relation;

    public RelationGetEvent(UserRelation relation) {
        super("Get relation success");

        this.relation = relation;
    }

    public RelationGetEvent(String message) {
        super(message);
    }

    public UserRelation getRelation() {
        return relation;
    }
}
