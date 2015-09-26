package cliq.com.cliqgram.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Feed;
import cliq.com.cliqgram.viewHolders.FeedViewHolder;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class FeedAdapter extends RecyclerView
        .Adapter<FeedViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;


    private Context context;
    private List<Feed> feedList;


    public FeedAdapter(Context context, List<Feed> feedList) {

        this.feedList = feedList;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout
                .feed_card_view, parent, false);
        FeedViewHolder feedViewHolder = new FeedViewHolder(context, view);
        return feedViewHolder;
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

    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder, int position) {

        runEnterAnimation(feedViewHolder.itemView, position);

        Feed feed = feedList.get(position);

        Log.e("FeedAdapter", feed.toString());

        feedViewHolder.feed_photo.setImageResource(feed.getPhotoId());
//        feedViewHolder.feed_comments.setText(feed.getComments().get(0)
//                .getContent());

        updateUserProfile(feedViewHolder);
        updateLikesCounter(feedViewHolder, false);

        // add tag to btn
        feedViewHolder.feed_btn_like.setTag(feedViewHolder);
        feedViewHolder.feed_btn_more.setTag(position);
        feedViewHolder.feed_btn_comments.setTag(position);
        feedViewHolder.feed_photo.setTag(feedViewHolder);

        this.bindClickListener(feedViewHolder, this.onClickListener);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    /**
     * @param feedViewHolder
     */
    private void updateUserProfile(FeedViewHolder feedViewHolder) {

        int position = feedViewHolder.getAdapterPosition();

        Feed feed = feedList.get(position);

        Bitmap bm_avatar = feedViewHolder.decodeResource(feed.getUser()
                .getAvatar_id());
        Bitmap resized_avatar = feedViewHolder.resizeBitmap(bm_avatar, 72, 72);

        feedViewHolder.feed_avatar.setImageBitmap(resized_avatar);
        feedViewHolder.feed_user_name.setText(feed.getUser().getUsername());
        feedViewHolder.feed_time.setText(feed.getDateString("d.MMM HH:mm"));

    }

    /**
     * @param feedViewHolder
     * @param animated
     */
    private void updateLikesCounter(FeedViewHolder feedViewHolder, boolean animated) {

        int position = feedViewHolder.getAdapterPosition();
        Feed feed = feedList.get(position);

        int currentLikesCount = feed.getLikes_count();
        String likesCountText = context.getResources().getQuantityString(
                R.plurals.feed_likes_count, currentLikesCount, currentLikesCount
        );

        if (animated) {
            feedViewHolder.feed_likes_count.setText(likesCountText);
        } else {
            feedViewHolder.feed_likes_count.setCurrentText(likesCountText);
        }
//        Log.e("UpdateLikesCount ", feedList.get(position).toString());
    }

    private void updateHeartButton(FeedViewHolder feedViewHolder) {

        Bitmap bm_btn_like = feedViewHolder.decodeResource(R.drawable.ic_heart_red);
        Bitmap resized_like = feedViewHolder.resizeBitmap(bm_btn_like,
                FeedViewHolder.BUTTON_WIDTH, FeedViewHolder.BUTTON_HEIGHT);
        feedViewHolder.feed_btn_like.setImageBitmap(resized_like);
    }


    /**
     *
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.feed_btn_like:
//                    Log.d("Button Like", "Button like clicked");
                    FeedViewHolder feedViewHolder = (FeedViewHolder) view.getTag();
                    Feed feed = feedList.get(feedViewHolder
                            .getAdapterPosition());
                    boolean isIncrement = feed.incrementLikes();
                    if (isIncrement) {
                        updateHeartButton(feedViewHolder);
                        updateLikesCounter(feedViewHolder, true);
                    }
                    break;
                case R.id.feed_btn_comments:

                    break;

                case R.id.feed_btn_more:

                    break;

                case R.id.feed_photo:

                    break;

                default:

                    Log.e("Feed Adapter", String.valueOf(id) + " not found");
            }
        }
    };

    /**
     * @param feedViewHolder
     * @param onClickListener
     */
    private void bindClickListener(FeedViewHolder feedViewHolder, View
            .OnClickListener onClickListener) {

        feedViewHolder.feed_btn_like.setOnClickListener(onClickListener);
        feedViewHolder.feed_btn_more.setOnClickListener(onClickListener);
        feedViewHolder.feed_btn_comments.setOnClickListener(onClickListener);
        feedViewHolder.feed_photo.setOnClickListener(onClickListener);

    }
}
