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
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.PostAdapter;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.CommentService;
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

        postList = new ArrayList<>();
        this.initializeData();
    }

    @Override
    public void onStart() {
        super.onStart();

        // register with EventBus
        AppStarter.eventBus.register(this);

    }

    @Override
    public void onStop() {
        AppStarter.eventBus.unregister(this);
        super.onStop();
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
        feedView.setAdapter(postAdapter);
    }

    private void initializeData() {

//        this.addFakeData();

        // followings list of currentUser
        String currentUsername = UserService.getCurrentUser().getUsername();

        UserRelationsService.getRelation(currentUsername, new GetCallback<UserRelation>() {
            @Override
            public void done(UserRelation relation, ParseException e) {
                if (e == null) {
                    List<User> followings = new ArrayList<>();

                    if (relation != null) {
                        followings.addAll(relation.getFollowings());
                    }

                    // also put current user's posts
                    followings.add(UserService.getCurrentUser());
                    PostService.getPosts(followings);
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
    }

    @Subscribe
    public void onPostDataReady(GetPostEvent event) {

        if (event.getPostList() != null) {
            postList = event.getPostList();
            postAdapter.updateFeedList(postList);
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


    public void addFakeData() {
        //
        User currentUser = UserService.getCurrentUser();

        User user1 = currentUser;

////        User user2 = User.userFactory("abc",
////                "abc@abc.com");
//
//        Post post1 = Post.createPost(Util.resizeDrawable(getActivity(), R.drawable
//                .lavery, 1f), user1, "good");
//        post1.setOwner( user1 );
////        post1.saveInBackground();
////
//////        Post post2 = Post.createPost(Util.resizeDrawable(getActivity(), R.drawable
//////                .lavery, 1f), user2, "yes");
//
//        Post post3 = Post.createPost(Util.resizeDrawable(getActivity(), R.drawable
//                .lavery, 1f), user1, "no");
//        post3.setOwner(user1);
//
//        feedList.add(post1);
//////        feedList.add(post2);
//        feedList.add(post3);

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        Comment comment = null;
        try {
            Post post = query.get("GKodyBu9mj");
            comment = Comment.createComment(user1, post,
                    "It's Good");
            CommentService.comment(post, comment);
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        //this is for testing follow operation
//        UserRelationsService.follow("litaos");
    }
}