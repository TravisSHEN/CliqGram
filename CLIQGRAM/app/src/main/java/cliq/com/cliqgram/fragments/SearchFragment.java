package cliq.com.cliqgram.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.SearchAdapter;
import cliq.com.cliqgram.adapters.UserSuggestAdapter;
import cliq.com.cliqgram.events.UserSuggestionRetrieved;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.UserService;
import de.greenrobot.event.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    @Bind(R.id.search_recycler_view)
    RecyclerView search_recycler_view;

    UserSuggestAdapter userSuggestAdapter;

    /**
     * user list storing all able suggesting users
     */
    List<User> userList;

    List<User> suggestList;

    /**
     * check if userList is loaded
     */
    boolean isUserListReady;

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

        // enable option menu
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
    }

    @Override
    public void onStart() {

        super.onStart();

        AppStarter.eventBus.register(this);


        // retrieve all users at beginning for search
        UserService.getAllUsers(new FindCallback<User>() {
            @Override
            public void done(List<User> userList, ParseException e) {
                if (e == null) {

                    SearchFragment.this.setUserList(userList);
                    SearchFragment.this.setIsUserListReady(true);

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast
                            .LENGTH_SHORT).show();

                    SearchFragment.this.setIsUserListReady(false);
                }
            }
        });


        // retrieve all suggested users
        UserService.getSuggestUsers();
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
        View root_view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, root_view);

        initializeSuggestView();

        return root_view;
    }


    private void initializeSuggestView() {
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity(),
                LinearLayoutManager.VERTICAL, false);
        search_recycler_view.setLayoutManager(llm);
        search_recycler_view.setHasFixedSize(true);

        userSuggestAdapter = new UserSuggestAdapter(getActivity(), suggestList);
        userSuggestAdapter.setFragmentManager( getFragmentManager() );
        search_recycler_view.setAdapter(userSuggestAdapter);
    }

    @Subscribe
    public void userSuggestionRetrieved( UserSuggestionRetrieved event){

        this.suggestList = event.getUserSuggestionList();
        if(this.suggestList == null ){
            return;
        }

        for( User user : suggestList ){
           Log.e("SearchFragment-Suggest", user.getUsername());
        }
        userSuggestAdapter.updateSuggestList(this.suggestList);
    }

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

            SearchView action_search = (SearchView) searchItem.getActionView();
            action_search.setIconifiedByDefault(true);

            action_search.setSearchableInfo(manager.getSearchableInfo
                    (getActivity().getComponentName()));

            action_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {

                    if (SearchFragment.this.isUserListReady()) {
                        doSearch(menu, query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    if (SearchFragment.this.isUserListReady()) {
                        doSearch(menu, query);
                    }
                    return true;
                }
            });

        }
    }

    private void doSearch(Menu menu, String query) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // normalize query
            String normalizedQuery = query.toLowerCase().trim();

            // userList
            List<User> userList = SearchFragment.this.getUserList();

            // list of results
            List<User> resultList = new ArrayList<>();

            // current user
            User currentUser = UserService.getCurrentUser();

            // Cursor
            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "default"};

            MatrixCursor cursor = new MatrixCursor(columns);

            int index = 0;
            for (int i = 0; i < userList.size(); i++) {


                User user = userList.get(i);

                Log.e("User Searchable", user.getUsername());

                if (user == null ||
                        user.getObjectId()
                                .equals(currentUser.getObjectId())) {
                    break;
                }

                String normalizedUsername = user.getUsername()
                        .toLowerCase()
                        .trim();

                if (normalizedUsername.contains(normalizedQuery)) {

                    temp[0] = index;
                    temp[1] = user;
                    cursor.addRow(temp);
                    resultList.add(user);

                    index++;
                }
            }

            final SearchView search = (SearchView) menu
                    .findItem(R.id.action_search)
                    .getActionView();

            SearchAdapter searchAdapter = new SearchAdapter(this.getActivity(),
                    cursor,
                    resultList);
            searchAdapter.setFragmentManager(getFragmentManager());
            searchAdapter.setSearchView(search);

            search.setSuggestionsAdapter(searchAdapter);
        }
    }

    public synchronized List<User> getUserList() {
        return userList;
    }

    public synchronized void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public synchronized boolean isUserListReady() {
        return isUserListReady;
    }

    public synchronized void setIsUserListReady(boolean isUserListReady) {
        this.isUserListReady = isUserListReady;
    }
}
