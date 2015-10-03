package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.FeedAdapter;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.CommentService;
import cliq.com.cliqgram.services.PostService;
import cliq.com.cliqgram.services.UserRelationsService;
import cliq.com.cliqgram.services.UserService;
import de.greenrobot.event.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    private FeedAdapter feedAdapter;

    private List<Post> feedList;

    @Bind(R.id.feed_recycler_view)
    RecyclerView feedView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserFeed.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AppStarter.eventBus.isRegistered(this)) {
            // register this class with EventBus
            AppStarter.eventBus.register(this);
        }

        feedList = new ArrayList<>();
        this.initializeData();
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


        feedAdapter = new FeedAdapter(this.getActivity(), feedList);
        feedView.setAdapter(feedAdapter);
    }

    private void initializeData() {

//        this.addFakeData();

//        //this is for testing followings list
        List<ParseUser> followings = UserRelationsService.getRelation
                (UserService.getCurrentUser().getUsername(),
                        "followings");
        PostService.getPosts(followings);
        //this is for testing followers list
//        List<ParseUser> followers =  UserRelationsService.getRelation
//                ("litaos",
//                "followers");

    }

    @Subscribe
    public void onPostDataReady(GetPostEvent event) {

        if (event.getPostList() != null) {
            feedList = event.getPostList();
            feedAdapter.updateFeedList(feedList);
        } else {
            Toast.makeText(this.getActivity(), event.getMessage(), Toast
                    .LENGTH_LONG).show();
        }

    }


    public void addFakeData(){
        //
        ParseUser currentUser = ParseUser.getCurrentUser();

        User user1 = UserService.getUserFromParseUser( currentUser );

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
