package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.ProfileAdapter;
import cliq.com.cliqgram.app.AppStarter;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.RelationGetEvent;
import cliq.com.cliqgram.events.UserGetEvent;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.services.PostService;
import cliq.com.cliqgram.services.UserRelationsService;
import cliq.com.cliqgram.services.UserService;
import de.greenrobot.event.Subscribe;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERID = "userId";

    // TODO: Rename and change types of parameters
    private String userId;

    @Bind(R.id.profile_avatar)
    CircleImageView profile_avatar;
    @Bind(R.id.profile_username)
    TextView profile_username;
    @Bind(R.id.profile_posts)
    TextView profile_posts_number;
    @Bind(R.id.profile_followers)
    TextView profile_followers;
    @Bind(R.id.profile_followings)
    TextView profile_followings;

    @Bind(R.id.profile_btn_follow)
    Button profile_follow_button;

    @Bind(R.id.profile_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.profile_recycler_view)
    RecyclerView profile_posts;


    private ProfileAdapter profileAdapter;

    private List<Post> postList;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId String.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USERID);
        }

        postList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_profile, container,
                false);

        ButterKnife.bind(this, root_view);

        this.initializeProfileView();

        return root_view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // initialize data
        this.initializeData(userId);

        // register with EventBus
        AppStarter.eventBus.register(this);

    }

    @Override
    public void onStop() {
        AppStarter.eventBus.unregister(this);
        super.onStop();
    }

    private void initializeProfileView() {
        GridLayoutManager glm = new GridLayoutManager(this.getActivity(), 3);
        profile_posts.setLayoutManager(glm);
        profile_posts.setHasFixedSize(true);

        // set refresh listener for SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeData(userId);
            }
        });

        profileAdapter = new ProfileAdapter(this.getActivity(), this.postList);
        profile_posts.setAdapter(profileAdapter);

        if (userId.equals(UserService.getCurrentUser().getObjectId())) {
            profile_follow_button.setVisibility(View.INVISIBLE);
        } else {
            profile_follow_button.setVisibility(View.VISIBLE);
        }
    }

    public void initializeData(String userId) {

        // find such user and put its data onto profile
        UserService.getUserById(userId);
    }


    @Subscribe
    public void onUserGetEvent(UserGetEvent event) {
        final User user = event.getUser();

        if (user == null) {
            Toast.makeText(getActivity(), "User not found.", Toast
                    .LENGTH_SHORT).show();
            return;
        }

        // load posts of such user
        PostService.getPosts(user);

        // get user's relations to count figures
        UserRelationsService.getRelation(user.getUsername());

        // load avatar of user to view
        user.loadAvatarToView(getActivity(), profile_avatar);

        profile_username.setText(user.getUsername());

        // hide refresh progress
        swipeRefreshLayout.setRefreshing(false);

    }

    @Subscribe
    public void onPostGetEvent(GetPostEvent event) {

        List<Post> postList = event.getPostList();

        if (postList == null) {
            return;
        }

        this.renderPosts(postList);
    }

    public void renderPosts(List<Post> postList) {

        this.profile_posts_number.setText(
                String.valueOf(postList.size()));
        this.postList = postList;
        this.profileAdapter.updatePostList(this.postList);

    }

    @Subscribe
    public void onRelationGetEvent(RelationGetEvent event) {
        UserRelation relation = event.getRelation();

        if (relation == null) {
            Toast.makeText(getActivity(), "User's relations not found.", Toast
                    .LENGTH_SHORT).show();
            return;
        }

        List<User> followings = relation.getFollowings();
        List<User> followers = relation.getFollowers();

        if (followings != null) {
            profile_followings.setText(String.valueOf(followings.size()));
        }
        if (followers != null) {
            profile_followers.setText(String.valueOf(followers.size()));
            /**
             * if current user already existing in follower list of such
             * user, then set profile_follow_button with "following" status.
             */
            if (followers.contains(UserService.getCurrentUser())) {
                profile_follow_button.setText(R.string.profile_following);
            }
        }

    }

    @OnClick({R.id.profile_btn_follow})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.profile_btn_follow:
                if (profile_follow_button.getText().equals("following") ||
                        userId.equals(UserService.getCurrentUser().getObjectId())) {
                    return;
                }

                UserService.getUserById(userId, new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {

                        if (e == null) {

                            UserRelationsService.follow(UserService.getCurrentUser(), object.getUsername());
                            profile_follow_button.setText(R.string.profile_following);
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

}
