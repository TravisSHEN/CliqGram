package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.fragments.FollowingFragment;
import cliq.com.cliqgram.fragments.YouFragment;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class ActivityViewPageAdapter extends FragmentStatePagerAdapter{

    Context context;

    List<TabItem> tabItems;


    public ActivityViewPageAdapter(Context context, FragmentManager fm, String userName) {
        super(fm);

        tabItems = new ArrayList<>();
        tabItems.add(new TabItem(FollowingFragment.newInstance(userName), "FOLLOWING"));
        tabItems.add(new TabItem(YouFragment.newInstance(userName), "YOU"));
    }

    @Override
    public Fragment getItem(int position) {
        return tabItems.get(position).fragment;
    }

    @Override
    public int getCount() {
        return tabItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabItems.get(position).title;
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
