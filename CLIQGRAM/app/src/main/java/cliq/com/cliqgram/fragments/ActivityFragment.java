package cliq.com.cliqgram.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.ActivityViewPageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ACTIONBAR = "actionbar";
    private static final String ARG_USERNAME = "userName";

    private FragmentManager fm;

    @Bind(R.id.activity_tab_layout)
    TabLayout activity_tabLayout;
    @Bind(R.id.activity_view_pager)
    ViewPager viewPager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityFragment newInstance(String userName) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
//        args.putSerializable(ARG_ACTIONBAR, (Serializable) actionBar);
        args.putString(ARG_USERNAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_activity, container, false);

        // bind view with ButterKnife
        ButterKnife.bind(this, root_view);

//        activity_tabLayout.addTab(activity_tabLayout.newTab().setText
//                ("FOLLOWING"));
//        activity_tabLayout.addTab(activity_tabLayout.newTab().setText
//                ("YOU"));
        ActivityViewPageAdapter pageAdapter = new ActivityViewPageAdapter
                (this.getActivity(), fm, this.getArguments().get(ARG_USERNAME).toString());
        viewPager.setAdapter(pageAdapter);
        activity_tabLayout.setupWithViewPager(viewPager);

        return root_view;
    }

}
