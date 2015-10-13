package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.PostAdapter;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.helper.BluetoothHelper;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.PostService;
import cliq.com.cliqgram.services.UserRelationsService;
import cliq.com.cliqgram.services.UserService;
import de.greenrobot.event.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private PostAdapter postAdapter;

    private List<Post> postList;

    private List<User> followings = new ArrayList<>();

    @Bind(R.id.feed_recycler_view)
    RecyclerView feedView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserFeed.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register with EventBus
        AppStarter.eventBus.register(this);

        postList = new ArrayList<>();
        this.initializeData();
    }

    @Override
    public void onDestroy() {
        AppStarter.eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        ButterKnife.bind(this, view);

        this.initializeFeedView();


        return view;
    }

    /**
     * initialize adapter for recycler view
     */
    private void initializeFeedView() {

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity(),
                LinearLayoutManager.VERTICAL, false);
        feedView.setLayoutManager(llm);
        feedView.setHasFixedSize(true);


        postAdapter = new PostAdapter(this.getActivity(), postList);
        postAdapter.setmBluetoothHelper(BluetoothHelper.getInstance());
        feedView.setAdapter(postAdapter);
    }

    private void initializeData() {

        // followings list of currentUser
        final String currentUsername = UserService.getCurrentUser().getUsername();

        // also put current user's posts
        this.followings.add(UserService.getCurrentUser());

        UserRelationsService.getRelation(currentUsername, new GetCallback<UserRelation>() {
            @Override
            public void done(UserRelation relation, ParseException e) {
                if (e == null) {

                    if (relation != null && relation.getFollowings() != null) {
                        PostFragment.this.followings.addAll(relation
                                .getFollowings());
                    }

                } else {
                    Toast.makeText(getActivity(), "No followings found for "
                            + currentUsername, Toast
                            .LENGTH_SHORT).show();
                }

                PostService.getPosts(PostFragment.this.followings);
            }
        });
    }

    @Subscribe
    public void onPostDataReady(GetPostEvent event) {

        if (event.getPostList() != null) {
            postList = event.getPostList();
            postAdapter.updateFeedList(postList);
        } else if (event.getPost() != null) {
            Post post = event.getPost();
            if (post != null) {

                Toast.makeText(getActivity(), "Received Post - " + post
                        .getObjectId(), Toast.LENGTH_SHORT).show();
                postList.add(post);
                postAdapter.addToFeedList(post);
            }
        } else {
            Toast.makeText(this.getActivity(), event.getMessage(), Toast
                    .LENGTH_LONG).show();
        }

    }

    @Subscribe
    public void onPostSuccessEvent(PostSuccessEvent event) {
        Post post = event.getPost();
        if (post != null) {
            postList.add(post);
            postAdapter.updateFeedList(postList);
        }
    }

    /**
     * add post to PostFragment
     *
     * @param postId
     */
    public void addPost(String postId) {

        PostService.getPost(postId);

    }

}
