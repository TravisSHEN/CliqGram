package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.FollowingActivityAdapter;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.services.ActivityService;
import cliq.com.cliqgram.services.UserRelationsService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {
    private static final String ARG_USERNAME = "userName";
    //List<Activity> activityList = new ArrayList<>();

    @Bind(R.id.activity_following_recycler_view)
    RecyclerView recyclerView;

    FollowingActivityAdapter followingActivityAdapter;

    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance(String userName) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        initializeData();

        //List<User> relation = UserRelationsService.getParticularRelation()

        /*ActivityService.pullFollowingActivity(newe Findijlk(){
            this.activityList = dataGet;
            followingActivityAdapter.updateData( this.activityList );
        })*/
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData() {

        // TODO: find post by id via post service
        UserRelationsService.getParticularRelation(this.getArguments().get(ARG_USERNAME).toString(), "followings", new GetCallback<UserRelation>() {
            @Override
            public void done(UserRelation object, ParseException e) {
                List<User> followings = object.getFollowings();
                ActivityService.pullFollowingActivity(followings, new FindCallback<Activity>() {
                    @Override
                    public void done(List<Activity> objects, ParseException e) {
                        followingActivityAdapter.updateData(objects);
                    }
                });
            }
        });
        //PostService.getPost(userName);
//        PostService.getPost("X8f2UlSJIc");
        //ProgressSpinner.getInstance().showSpinner(this.getActivity(), "Loading...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_following,
                container,
                false);

        // Bind view with ButterKnive
        ButterKnife.bind(this, root_view);

        this.initializeRecyclerView();

        return root_view;
    }

    /**
     * initialize adapter for recycler view
     */
    private void initializeRecyclerView() {

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);


        followingActivityAdapter = new FollowingActivityAdapter(this
                .getActivity());
        recyclerView.setAdapter(followingActivityAdapter);
    }

}
