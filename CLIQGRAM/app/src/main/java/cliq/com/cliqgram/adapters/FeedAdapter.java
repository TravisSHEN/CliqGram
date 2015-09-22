package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Feed;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class FeedAdapter extends RecyclerView
        .Adapter<FeedAdapter.FeedViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;


    private Context context;
    private List<Feed> feedList;

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.feed_card_view)
        CardView cv;
        @Bind(R.id.feed_photo)
        ImageView feed_photos;
        @Bind(R.id.feed_likes)
        TextView feed_likes;
        @Bind(R.id.feed_comments)
        TextView feed_comments;

        FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public FeedAdapter(List<Feed> feedList, Context context) {

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
        FeedViewHolder feedViewHolder = new FeedViewHolder(view);
        return feedViewHolder;
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
//            view.setTranslationY(UtilgetScreenHeight(context));
            view.setTranslationY(200);
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

        feedViewHolder.feed_photos.setImageResource(feed.getPhotoId());
//        feedViewHolder.feed_likes.setText(feed.getLikes_count());
//        feedViewHolder.feed_comments.setText(feed.getComment().getContent());


    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
