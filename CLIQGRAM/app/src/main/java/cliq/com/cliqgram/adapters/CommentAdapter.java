package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.viewHolders.FeedViewHolder;

/**
 * Created by litaoshen on 27/09/2015.
 */
public class CommentAdapter extends RecyclerView
        .Adapter<FeedViewHolder>{

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
