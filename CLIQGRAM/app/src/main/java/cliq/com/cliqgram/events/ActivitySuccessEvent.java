package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.Activity;

/**
 * Created by ilkan on 11/10/2015.
 */
public class ActivitySuccessEvent extends BaseEvent {

    Activity activity;

    public ActivitySuccessEvent(Activity activity){
        super("Activity recorded successfully");
        this.activity = activity;
    }
    public ActivitySuccessEvent(String message){
        super(message);
    }
    public Activity getActivity(){
        return this.activity;
    }
}
