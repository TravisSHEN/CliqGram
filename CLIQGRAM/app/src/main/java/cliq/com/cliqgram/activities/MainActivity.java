package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.StarterApplication;
import cliq.com.cliqgram.events.FABLongClickEvent;
import cliq.com.cliqgram.events.OpenCommentEvent;
import cliq.com.cliqgram.fragments.ActivityFragment;
import cliq.com.cliqgram.fragments.CommentFragment;
import cliq.com.cliqgram.fragments.FeedFragment;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.fragments.SettingFragment;
import cliq.com.cliqgram.helper.ToolbarModel;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAVIGATION_ITEM_ID = "navigationItemID";

    @Bind(R.id.mDrawer)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.btn_float_action)
    FloatingActionButton float_button;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavSelectedItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // inject views
        ButterKnife.bind(this);

        // Register this activity to EventBus
        StarterApplication.BUS.register(this);

        // setup toolbar
        ToolbarModel.setupToolbar(this);

        // set click listener to navigation view
        navigationView.setNavigationItemSelectedListener(this);

        // initialize showing fragment
        this.showInitialSelectedFragment(savedInstanceState);

        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R
                .string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            // Log out current user
            if (ParseUser.getCurrentUser() != null) {
                // do stuff with the user
                ParseUser.logOut();
            }

            // jump back to login activity
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // store current selected navigation item state
        outState.putInt(NAVIGATION_ITEM_ID, mNavSelectedItemID);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        this.showFragment(menuItem);

        return true;
    }


    //    @OnItemSelected( R.id.navigation_view )
    void showFragment(MenuItem menuItem) {

        // close drawer when select one item
        mDrawerLayout.closeDrawers();

        menuItem.setChecked(true);
        // set current selected navigation item id
        mNavSelectedItemID = menuItem.getItemId();

        // open corresponding fragment
        Fragment fragment = null;
        String title = "";

        switch (menuItem.getItemId()) {
            case R.id.navigation_item_home:

                fragment = FeedFragment.newInstance();
                title = getString(R.string.navigation_item_home);

                Snackbar.make(toolbar, "Home selected", Snackbar
                        .LENGTH_SHORT)
                        .show();
                break;
            case R.id.navigation_item_profile:

                fragment = new ProfileFragment();
                title = getString(R.string.navigation_item_profile);

                Snackbar.make(toolbar, "Profile selected", Snackbar
                        .LENGTH_SHORT)
                        .show();
                break;
            case R.id.navigation_item_activity:

                fragment = new ActivityFragment();
                title = getString(R.string.navigation_item_activity);

                Snackbar.make(toolbar, "Activity selected", Snackbar
                        .LENGTH_SHORT)
                        .show();
                break;
            case R.id.navigation_item_setting:

                fragment = new SettingFragment();
                title = getString(R.string.navigation_item_setting);

                Snackbar.make(toolbar, "Setting selected", Snackbar.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }

        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_body, fragment)
                    .commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void showInitialSelectedFragment(Bundle savedInstanceState) {

        // if there is no pre-selected item,
        // show home fragment
        if (savedInstanceState == null) {
            mNavSelectedItemID = R.id.navigation_item_home;
        } else {
            mNavSelectedItemID = savedInstanceState.getInt(NAVIGATION_ITEM_ID);
        }
        MenuItem menuItem = navigationView.getMenu().findItem(mNavSelectedItemID);
        this.showFragment(menuItem);
    }

    @OnLongClick(R.id.btn_float_action)
    boolean onLongClick(View view) {

        StarterApplication.BUS.post(new FABLongClickEvent());
        Toast.makeText(this, "Long click", Toast.LENGTH_SHORT)
                .show();
        return true;
    }

    @OnClick(R.id.btn_float_action)
    void onClick(View view) {

        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
//        StarterApplication.BUS.post(new FABClickEvent());
        Toast.makeText(this, "Short click", Toast.LENGTH_SHORT)
                .show();
    }

    @Subscribe
    public void onOpenCommentFragment(final OpenCommentEvent
                                                  openFragmentEvent) {

        CommentFragment fragment = CommentFragment.newInstance();
        String title = getString(R.string.navigation_item_comment);

        Snackbar.make(toolbar, "Comment opened", Snackbar
                .LENGTH_SHORT)
                .show();

        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_body, fragment)
                    .commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}
