package cliq.com.cliqgram.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.utils.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 24/09/2015.
 */

public class FeedViewHolder extends RecyclerView.ViewHolder {

    public static int BUTTON_WIDTH = 48;
    public static int BUTTON_HEIGHT = 48;

    public Context context;

    @Bind(R.id.feed_card_view)
    public CardView cv;
    @Bind(R.id.feed_user_avatar)
    public CircleImageView feed_avatar;
    @Bind(R.id.feed_user_name)
    public TextView feed_user_name;
    @Bind(R.id.feed_time)
    public TextView feed_time;
    @Bind(R.id.feed_photo)
    public ImageView feed_photo;
    @Bind(R.id.feed_btn_like)
    public ImageButton feed_btn_like;
    @Bind(R.id.feed_btn_comments)
    public ImageButton feed_btn_comments;
    @Bind(R.id.feed_btn_more)
    public ImageButton feed_btn_more;
    @Bind(R.id.feed_ic_likes_count)
    public ImageView feed_ic_likes_count;
    @Bind(R.id.feed_likes_count)
    public TextSwitcher feed_likes_count;
    @Bind(R.id.feed_comments)
    public TextView feed_comments;

    public FeedViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);

        initializeButtons();
    }

    void initializeButtons() {

        Bitmap bm_btn_like = ImageUtil.decodeResource(context, R.drawable
                .ic_heart_outline_grey);
        Bitmap resized_like = ImageUtil.resizeBitmap(bm_btn_like, FeedViewHolder
                .BUTTON_WIDTH, FeedViewHolder.BUTTON_HEIGHT);
        feed_btn_like.setImageBitmap(resized_like);
        Bitmap bm_btn_comments = ImageUtil.decodeResource(context, R.drawable.ic_comment_outline_grey);
        Bitmap resized_comments = ImageUtil.resizeBitmap(bm_btn_comments, FeedViewHolder.BUTTON_WIDTH, FeedViewHolder.BUTTON_HEIGHT);
        feed_btn_comments.setImageBitmap(resized_comments);
        Bitmap bm_btn_more = ImageUtil.decodeResource(context, R.drawable.ic_more_grey);
        Bitmap resized_more = ImageUtil.resizeBitmap(bm_btn_more, FeedViewHolder.BUTTON_WIDTH, FeedViewHolder.BUTTON_HEIGHT);
        feed_btn_more.setImageBitmap(resized_more);

        Bitmap bm_ic_likes = ImageUtil.decodeResource(context, R.drawable
                .ic_heart_small_blue);
        Bitmap resized_ic_likes = ImageUtil.resizeBitmap(bm_ic_likes, FeedViewHolder.BUTTON_WIDTH, FeedViewHolder.BUTTON_HEIGHT);
        feed_ic_likes_count.setImageBitmap(resized_ic_likes);
    }

}


