package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.views.SquareImageView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 5/10/2015.
 */
public class SearchAdapter extends CursorAdapter {

    private FragmentManager fragmentManager;
    private SearchView searchView;

    private List<User> userList;

    // text view for showing results
    @Bind(R.id.search_result)
    LinearLayout search_result;
    @Bind(R.id.search_username)
    TextView text_username;
    @Bind(R.id.search_user_avatar)
    CircleImageView search_avatar;
    @Bind(R.id.search_post_image)
    SquareImageView post_image;


    public SearchAdapter(Context context, MatrixCursor cursor, List<User>
            userList) {
        super(context, cursor, false);

        if (userList == null) {
            this.userList = new ArrayList<>();
        } else {
            this.userList = userList;
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater
                .inflate(R.layout.search_result_item, parent, false);

        // bind view to ButterKnife
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final User user = userList.get(cursor.getPosition());
        if (user == null) {
            return;
        }

        // set tag to text_username
        text_username.setTag(user);

        search_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               click(user);
            }
        });

        text_username.setText(user.getUsername());
        Bitmap avatar = user.getAvatarBitmap();
        search_avatar.setImageBitmap(avatar);
    }

//    @OnClick(R.id.search_username)
    public void click(User user) {

        Log.e("SearchAdapter", "Clicked");
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
                .addToBackStack(null)
                .commit();

        getSearchView().clearFocus();

    }


    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

}
