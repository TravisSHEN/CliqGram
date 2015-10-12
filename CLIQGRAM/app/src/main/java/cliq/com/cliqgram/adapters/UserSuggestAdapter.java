package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.viewHolders.UserSuggestViewHolder;

/**
 * Created by litaoshen on 7/10/2015.
 */
public class UserSuggestAdapter extends RecyclerView.Adapter<UserSuggestViewHolder> {

    private FragmentManager fragmentManager;

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

        final User user = userSuggestList.get(position);

        user.loadAvatarToView( context, holder.avatar);
        holder.username.setText(user.getUsername());

        holder.suggest_user.setTag(user);
        holder.suggest_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click( user );
            }
        });

        // TODO: show most recent post photo
//        holder.image.setImageBitmap(photo);
        List<Post> postList = user.getPostList();
        int size = postList.size();
        Post post = postList.get( size -1 );
        post.loadPhotoToView( context, holder.image);

    }

    @Override
    public int getItemCount() {
        return (userSuggestList == null || userSuggestList.isEmpty()) ?  0:
                userSuggestList.size();
    }

    public void updateSuggestList(List<User> userSuggestList){
        this.userSuggestList = userSuggestList;

        this.notifyDataSetChanged();
    }

        public void click(User user) {

//        Log.e("SearchAdapter", "Clicked");
        User selectedUser = user;

//        User selectedUser = (User) text_username.getTag();
        if(selectedUser == null ){
            return;
        }

        String userId = selectedUser.getObjectId();

        Fragment profileFragment = ProfileFragment.newInstance(userId);
        View view = profileFragment.getView();
        if(view != null){
            FrameLayout layout = (FrameLayout) view.findViewById(R.id.fragment_profile);
            layout.setPadding(0,0,0,0);
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.search_container, profileFragment,
                        userId + "ProfileFragment")
                .addToBackStack(userId + "ProfileFragment")
                .commit();
    }


    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}
