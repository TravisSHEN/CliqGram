package cliq.com.cliqgram.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.SearchAdapter;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.services.UserRelationsService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    @Bind(R.id.search_recycler_view)
    RecyclerView search_recycler_view;

    /**
     * user list storing all able suggesting users
     */
    List<User> userList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        // enable option menu
        setHasOptionsMenu(true);

        userList = UserRelationsService.getRelation("jj", "followings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, root_view);

        return root_view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // register with EventBus
//        AppStarter.eventBus.register(this);
//
//    }
//
//    @Override
//    public void onStop() {
//        AppStarter.eventBus.unregister(this);
//        super.onStop();
//    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        setupActionSearch(menu);
    }

    /**
     * set up action search on ActionBar
     *
     * @param menu
     */
    public void setupActionSearch(final Menu menu) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager)
                    getActivity()
                            .getSystemService(Context
                                    .SEARCH_SERVICE);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(true);
            searchItem.setIcon(R.drawable.icon_search);

            SearchView searchView = new SearchView(this.getActivity());
            searchItem.setActionView(searchView);

            SearchView action_search = (SearchView) searchItem.getActionView();

            action_search.setVisibility(View.VISIBLE);

            action_search.setSearchableInfo(manager.getSearchableInfo
                    (getActivity().getComponentName()));

            action_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {

                    loadHistory(menu, query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    loadHistory(menu, query);
                    return true;
                }

            });

        }
    }

    private void loadHistory(Menu menu, String query) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor
            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "default"};

            MatrixCursor cursor = new MatrixCursor(columns);

            for (int i = 0; i < userList.size(); i++) {

                temp[0] = i;
                temp[1] = userList.get(i);

                cursor.addRow(temp);

            }

            // SearchView
            SearchManager manager = (SearchManager)
                    getActivity()
                    .getSystemService(Context
                            .SEARCH_SERVICE);

            final SearchView search = (SearchView) menu
                    .findItem(R.id.action_search)
                    .getActionView();

            search.setSuggestionsAdapter(new SearchAdapter(this.getActivity(),
                    cursor,
                    userList));
        }
    }
}
