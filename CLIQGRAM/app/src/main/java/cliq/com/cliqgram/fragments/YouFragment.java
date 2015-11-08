package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.YouActivityAdapter;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.services.ActivityService;
import cliq.com.cliqgram.services.UserRelationsService;
import cliq.com.cliqgram.services.UserService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YouFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USERNAME = "userName";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    @Bind(R.id.activity_you_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.you_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    YouActivityAdapter youActivityAdapter;


    // TODO: Rename and change types and number of parameters
    public static YouFragment newInstance(String userName) {
        YouFragment fragment = new YouFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    public YouFragment() {
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

    }

    @Override
    public void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData() {

        // TODO: find post by id via post service
        UserRelationsService.getParticularRelation(this.getArguments().get(ARG_USERNAME).toString(),
                "followers", new GetCallback<UserRelation>() {
                    @Override
                    public void done(final UserRelation object, ParseException e) {

                        if (e == null) {
                            List<User> followers = object.getFollowers();
                            ActivityService.pullActivityRegardingToYou(UserService.getCurrentUser(),
                                    followers, new FindCallback<Activity>() {
                                        @Override
                                        public void done(List<Activity> objects, ParseException e) {
                                            if (objects == null) {
                                                Toast.makeText(getActivity(), e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            Collections.sort(objects);
                                            youActivityAdapter.updateData(objects);

                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast
                                    .LENGTH_SHORT).show();
                        }


                        // hide refresh progress
                        swipeRefreshLayout.setRefreshing(false);

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
        View root_view = inflater.inflate(R.layout.fragment_you, container, false);

        // Bind view to ButterKnife
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


        youActivityAdapter = new YouActivityAdapter(this
                .getActivity());
        recyclerView.setAdapter(youActivityAdapter);


        // set refresh listener for SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeData();
            }
        });
    }


}
