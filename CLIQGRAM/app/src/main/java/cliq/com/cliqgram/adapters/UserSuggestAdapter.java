package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.utils.Util;
import cliq.com.cliqgram.viewHolders.UserSuggestViewHolder;

/**
 * Created by litaoshen on 7/10/2015.
 */
public class UserSuggestAdapter extends RecyclerView.Adapter<UserSuggestViewHolder> {

    private Context context;
    private List<User> userSuggestList;

    public UserSuggestAdapter(Context context, List<User> userSuggestList) {
        this.context = context;
        this.userSuggestList = userSuggestList;
    }

    @Override
    public UserSuggestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout
                .suggest_item, parent, false);
        UserSuggestViewHolder userSuggestViewHolder = new
                UserSuggestViewHolder(context, view);
        return userSuggestViewHolder;
    }

    @Override
    public void onBindViewHolder(UserSuggestViewHolder holder, int position) {

        User user = userSuggestList.get(position);

        BitmapDrawable bitmapDrawable = Util.convertByteToBitmapDrawable
                (context, user.getAvatarData());
        holder.avatar.setImageDrawable( bitmapDrawable );

        holder.username.setText(user.getUsername());

        // TODO: show most recent post photo
//        holder.image.setImageBitmap(photo);
    }

    @Override
    public int getItemCount() {
        return (userSuggestList == null || userSuggestList.isEmpty()) ?  0:
                userSuggestList.size();
    }
}
