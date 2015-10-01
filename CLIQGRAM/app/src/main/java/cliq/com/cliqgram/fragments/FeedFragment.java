package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.FeedAdapter;
import cliq.com.cliqgram.model.Feed;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FeedAdapter feedAdapter;

    private List<Post> feedList;

    @Bind(R.id.feed_recycler_view)
    RecyclerView feedView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFeed.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        feedList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        ButterKnife.bind(this, view);

        this.initializeFeedView();
        this.initializeData();

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


        feedAdapter = new FeedAdapter(this.getActivity(), feedList );
        feedView.setAdapter(feedAdapter);
    }

    private void initializeData() {

        ParseUser currentUser = ParseUser.getCurrentUser();

        User user1 = User.userFactory(currentUser.getUsername(),
                currentUser.getEmail());

        User user2 = User.userFactory("abc",
                "abc@abc.com");

        feedList.add(Post.createPost(Util.resizeDrawable(getActivity(), R.drawable
                .lavery, 1f), user1, "good"));
        feedList.add(Post.createPost(Util.resizeDrawable(getActivity(), R.drawable
                .lavery, 1f), user2, "yes"));
        feedList.add(Post.createPost(Util.resizeDrawable(getActivity(), R.drawable
                .lavery, 1f), user1, "no"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.DATE, 1);

        feedList.get(2).setCreatedAt(calendar.getTime());

        Collections.sort(feedList);

        for(Post post: feedList){
            Log.d("Feed", post.toString());
        }
        feedAdapter.notifyDataSetChanged();
    }

}
