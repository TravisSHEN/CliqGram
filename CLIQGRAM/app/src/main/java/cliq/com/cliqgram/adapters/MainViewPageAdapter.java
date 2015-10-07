package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.fragments.ActivityFragment;
import cliq.com.cliqgram.fragments.PostFragment;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.fragments.SearchFragment;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class MainViewPageAdapter extends FragmentStatePagerAdapter {

    Context context;
    List<TabItem> tabItemList;

    public MainViewPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        tabItemList = new ArrayList<>();
        initializeFragments();
    }

    private void initializeFragments() {
        tabItemList.add(new TabItem(PostFragment.newInstance(), "Feed"));
        tabItemList.add(new TabItem(SearchFragment.newInstance(), "Search"));
        // TODO: change this to CameraFragment
        tabItemList.add(new TabItem(PostFragment.newInstance(), "Camera"));
        tabItemList.add(new TabItem(ActivityFragment.newInstance(), "Activity"));
        tabItemList.add(new TabItem(ProfileFragment.newInstance(ParseUser
                .getCurrentUser().getObjectId()),
                "Profile"));
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = tabItemList.get(position).fragment;
        return fragment;
    }

    @Override
    public int getCount() {
        return tabItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String title = tabItemList.get(position).title;

        return "";
    }

    private static class TabItem {
        Fragment fragment;
        String title;

        public TabItem(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }
    }
}
