package cliq.com.cliqgram.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.events.OpenCommentEvent;
import cliq.com.cliqgram.model.Like;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.LikeService;
import cliq.com.cliqgram.utils.Util;
import cliq.com.cliqgram.viewHolders.FeedViewHolder;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class FeedAdapter extends RecyclerView
        .Adapter<FeedViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;


    private Context context;
    private List<Post> postList;


    public FeedAdapter(Context context, List<Post> postList) {

        this.postList = postList;
        this.context = context;

    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout
                .feed_card_view, parent, false);
        FeedViewHolder feedViewHolder = new FeedViewHolder(context, view);
        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder feedViewHolder, int position) {

        runEnterAnimation(feedViewHolder.itemView, position);

        final Post post = postList.get(position);

//        Log.e("FeedAdapter", feed.toString());

        post.getPhotoData(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                feedViewHolder.feed_photo.setImageBitmap(post
                        .getPhotoInBitmap(context, data));
            }
        });

        feedViewHolder.feed_comments.setText(post.getDescription());

//        feedViewHolder.feed_comments.setText(post.getCommentList().get(0)
//                .getContent());

        updateUserProfile(feedViewHolder);
        updateLikesCounter(feedViewHolder, false);

        // add tag to btn
        feedViewHolder.feed_btn_like.setTag(feedViewHolder);
        feedViewHolder.feed_btn_more.setTag(position);
        feedViewHolder.feed_btn_comments.setTag(post);
        feedViewHolder.feed_photo.setTag(feedViewHolder);

        this.bindClickListener(feedViewHolder, this.onClickListener, this.onTouchListener);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * @param feedViewHolder
     */
    private void updateUserProfile(FeedViewHolder feedViewHolder) {

        int position = feedViewHolder.getAdapterPosition();

        Post post = postList.get(position);

        BitmapDrawable bm_avatar = Util.resizeBitmapDrawable(context,
                post.getOwner().getAvatarInBitmapDrawable(context), 0.7f);
        feedViewHolder.feed_avatar.setImageDrawable(bm_avatar);
        feedViewHolder.feed_user_name.setText(post.getOwner().getUsername());
        // TODO:
        feedViewHolder.feed_time.setText(post.getDateString("d.MMM HH:mm"));

        List<Like> likeList = post.getLikeList();
        // highlight red heart button if user already liked.
        if (likeList != null && likeList.size() > 0 &&
                LikeService.isAlreadyLiked(ParseUser.getCurrentUser(), likeList)) {
            if( post.getObjectId().equals( "X5NTh1VrId")){
                for(Like like: post.getLikeList()){
                   Log.e("Like", like.getObjectId());
                    Log.e("Like", like.getUser().getUsername());
                    Log.e("Like", like.getPost().getObjectId());
                }
            }
            setHeartButtonLiked(feedViewHolder);
        }
    }

    /**
     * @param feedViewHolder
     * @param animated
     */
    private void updateLikesCounter(FeedViewHolder feedViewHolder, boolean animated) {

        int position = feedViewHolder.getAdapterPosition();
        Post post = postList.get(position);

        int currentLikesCount = post.getLikes_count();
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


    private void updateLikes(View view) {
        FeedViewHolder feedViewHolder = (FeedViewHolder) view.getTag();
        Post post = postList.get(feedViewHolder.getAdapterPosition());

        if(LikeService.isAlreadyLiked(ParseUser.getCurrentUser(), post.getLikeList())){
            post.unlike();
            setHeartButtonUnLiked(feedViewHolder);
        } else {
            post.incrementLikes();
            setHeartButtonLiked(feedViewHolder);
        }

        updateLikesCounter(feedViewHolder, true);
    }


    private void setHeartButtonLiked(FeedViewHolder feedViewHolder){

        Bitmap bm_btn_like = Util.decodeResource(context,
                R.drawable.ic_heart_red);
        Bitmap resized_like = Util.resizeBitmap(bm_btn_like,
                FeedViewHolder.BUTTON_WIDTH, FeedViewHolder.BUTTON_HEIGHT);
        feedViewHolder.feed_btn_like.setImageBitmap(resized_like);
    }

    private void setHeartButtonUnLiked(FeedViewHolder feedViewHolder){

        Bitmap bm_btn_like = Util.decodeResource(context,
                R.drawable.ic_heart_outline_grey);
        Bitmap resized_like = Util.resizeBitmap(bm_btn_like,
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
                    updateLikes(view);
                    break;
                case R.id.feed_btn_comments:

                    AppStarter.eventBus.post(new OpenCommentEvent((Post) view.getTag()));
                    break;

                case R.id.feed_btn_more:

                    break;

                default:

                    Log.e("Feed Adapter", String.valueOf(id) + " not found");
            }
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        boolean firstTouch = false;
        long time = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (v.getId()) {
                case R.id.feed_photo:
                    if (event.getAction() == event.ACTION_DOWN) {
                        Calendar calendar = Calendar.getInstance();
                        if (firstTouch &&
                                (calendar.getTimeInMillis() - time) <= 300) {
//                            Log.e("** DOUBLE TAP**"," second tap ");
                            firstTouch = false;

                            updateLikes(v);

                        } else {
                            firstTouch = true;
                            time = calendar.getTimeInMillis();
//                            Log.e("** SINGLE  TAP**"," First Tap time  "+time);
                            return false;
                        }
                    }
                    break;
                default:
                    Log.d("TouchListener", "No view found");
            }

            return true;
        }
    };

    /**
     * @param feedViewHolder
     * @param onClickListener
     */
    private void bindClickListener(FeedViewHolder feedViewHolder, View
            .OnClickListener onClickListener, View.OnTouchListener onTouchListener) {

        feedViewHolder.feed_btn_like.setOnClickListener(onClickListener);
        feedViewHolder.feed_btn_more.setOnClickListener(onClickListener);
        feedViewHolder.feed_btn_comments.setOnClickListener(onClickListener);

        feedViewHolder.feed_btn_comments.setTag(postList.get(feedViewHolder.getAdapterPosition()));

        feedViewHolder.feed_photo.setOnTouchListener(onTouchListener);

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


    public void updateFeedList(List<Post> feedList) {
        this.postList = feedList;

        Collections.sort(feedList);

        this.notifyDataSetChanged();
    }
}
