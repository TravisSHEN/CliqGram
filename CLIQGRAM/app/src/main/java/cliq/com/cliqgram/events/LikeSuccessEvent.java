package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.Like;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class LikeSuccessEvent extends BaseEvent{

    Like like;

    public LikeSuccessEvent(Like like) {
        super("Like successfully");
        this.like = like;
    }

    public LikeSuccessEvent(String msg){
        super(msg);
    }

    public Like getLike() {
        return like;
    }
}
