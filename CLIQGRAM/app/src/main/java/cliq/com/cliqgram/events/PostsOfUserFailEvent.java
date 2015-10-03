package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 3/10/2015.
 */
public class PostsOfUserFailEvent extends BaseEvent{

    public PostsOfUserFailEvent(String message){
        super(message);
    }
    public PostsOfUserFailEvent(){
        super("Posts of User Fail");
    }

}
