package cliq.com.cliqgram.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.utils.Util;
import cliq.com.cliqgram.viewHolders.CommentViewHolder;

/**
 * Created by litaoshen on 27/09/2015.
 */
public class CommentAdapter extends RecyclerView
        .Adapter<CommentViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;

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
//        Log.e("CommentFragment", "opened");
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        runEnterAnimation(holder.itemView, position);

        final Comment comment = commentList.get(position);

//        comment.getOwner().getAvatarData(new GetDataCallback() {
//            @Override
//            public void done(byte[] data, ParseException e) {
//                Bitmap bitmap = comment.getOwner()
//                        .getAvatarBitmap(context, data);
//                Bitmap bitmap_resized = Util.resizeBitmap(context,
//                        bitmap,
//                        0.7f);
//
//                holder.comment_user_avatar.setImageBitmap(bitmap_resized);
//            }
//        });

        BitmapDrawable bitmapDrawable = Util.convertByteToBitmapDrawable(context, comment
                .getOwner().getAvatarData());
        holder.comment_user_avatar.setImageDrawable( bitmapDrawable );

        holder.comment_user_name.setText(comment.getOwner().getUsername());
        holder.comment_text.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager()
                    .getDefaultDisplay().getMetrics(displayMetrics);
            view.setTranslationY(displayMetrics.heightPixels);
//            view.setTranslationY(200);
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    public void updateComments(List<Comment> commentList) {
        this.commentList = commentList;
        this.notifyDataSetChanged();
    }
//
//    public void updateComments(Comment comment){
//        this.commentList.add(comment);
//        this.notifyItemInserted(this.commentList.size()-1);
//    }
}
