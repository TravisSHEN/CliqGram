package cliq.com.cliqgram.events;

import com.parse.ParseObject;

import java.util.List;

import cliq.com.cliqgram.model.Comment;

/**
 * Created by ilkan on 29/09/2015.
 */
public class CommentSuccessEvent extends BaseEvent {

    private List<Comment> commentList;

    public CommentSuccessEvent(List<Comment> commentList){
        super("Comment success");
        this.commentList = commentList;
    }

    public CommentSuccessEvent(){
        super("Comment was successful!");
    }
    public CommentSuccessEvent(String message) {
        super(message);
    }
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
