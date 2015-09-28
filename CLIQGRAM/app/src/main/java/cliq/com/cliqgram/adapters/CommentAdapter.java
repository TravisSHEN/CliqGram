package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.Utils.Util;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.viewHolders.CommentViewHolder;

/**
 * Created by litaoshen on 27/09/2015.
 */
public class CommentAdapter extends RecyclerView
        .Adapter<CommentViewHolder>{

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comments_item,
                parent, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(context, view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        Bitmap bm_avatar = Util.decodeResource(context,
                comment.getUser().getAvatar_id());
        Bitmap resized_avatar = Util.resizeBitmap(bm_avatar, 72, 72);

        holder.comment_user_avatar.setImageBitmap(resized_avatar);
        holder.comment_user_name.setText(comment.getUser().getUsername());
        holder.comment_text.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
