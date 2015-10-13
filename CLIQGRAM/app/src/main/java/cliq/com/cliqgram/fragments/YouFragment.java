package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.FollowingActivityAdapter;
import cliq.com.cliqgram.adapters.YouActivityAdapter;
import cliq.com.cliqgram.viewHolders.YouActivityViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YouFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YouFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Bind(R.id.activity_you_recycler_view)
    RecyclerView recyclerView;

    YouActivityAdapter youActivityAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YouFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YouFragment newInstance() {
        YouFragment fragment = new YouFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public YouFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    }



}
