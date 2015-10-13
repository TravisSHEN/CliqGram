package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.MainViewPageAdapter;
import cliq.com.cliqgram.events.OpenCommentEvent;
import cliq.com.cliqgram.helper.ToolbarModel;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.Util;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    private static final String NAVIGATION_ITEM_ID = "navigationItemID";

    private static final int REQUEST_COMMENT = 0;

    public static final int TAB_FEED = 0;
    public static final int TAB_SEARCH = 1;
    public static final int TAB_CAMERA = 2;
    public static final int TAB_ACTIVITY = 3;
    public static final int TAB_PROFILE = 4;

    public static final String TAG_FEED_FRAGMENT = "TAG_FeedFragment";
    public static final String TAG_SEARCH_FRAGMENT = "TAG_SearchFragment";
    public static final String TAG_ACTIVITY_FRAGMENT = "TAG_ActivityFragment";
    public static final String TAG_PROFILE_FRAGMENT = "TAG_ProfileFragment";
    public static final String TAG_CAMERA_FRAGMENT = "TAG_CameraFragment";


    FragmentManager fm = getSupportFragmentManager();

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.btn_float_action)
    FloatingActionButton float_button;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavSelectedItemID;
    Button post;
    private static int PICKED_IMG = 1;
    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // inject views
        ButterKnife.bind(this);

        // setup toolbar
        ToolbarModel.setupToolbar(this);

        // initialize tab bar layout
        initializeTabLayout();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register this activity to EventBus
        AppStarter.eventBus.register(this);
    }

    @Override
    protected void onStop() {

        AppStarter.eventBus.unregister(this);
        super.onStop();
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
            if (UserService.getCurrentUser() != null) {
                // do stuff with the user
                UserService.logOut();
            }

            // jump back to login activity
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0) {
            float_button.setVisibility(View.VISIBLE);
            // enable moving back
            moveTaskToBack(false);
            fm.popBackStack();
        } else {
            // disable moving back
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // store current selected navigation item state
        outState.putInt(NAVIGATION_ITEM_ID, mNavSelectedItemID);
    }

    void initializeTabLayout() {
        MainViewPageAdapter mainViewPageAdapter = new MainViewPageAdapter(this, fm);
        viewPager.setAdapter(mainViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        float scale_factor = 0.7f;
        tabLayout.getTabAt(TAB_FEED).setIcon(Util.resizeDrawable(this,
                R.drawable.icon_home, scale_factor));
        tabLayout.getTabAt(TAB_SEARCH).setIcon(Util.resizeDrawable(this, R
                .drawable.icon_search, scale_factor));
//        tabLayout.getTabAt(TAB_CAMERA).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(TAB_CAMERA).setCustomView(float_button);
        tabLayout.getTabAt(TAB_ACTIVITY).setIcon(Util.resizeDrawable(this, R
                .drawable.icon_star, scale_factor));
        tabLayout.getTabAt(TAB_PROFILE).setIcon(Util.resizeDrawable(this, R
                .drawable.icon_user, scale_factor));
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

        String postId = openFragmentEvent.getPost().getObjectId();
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(CommentActivity.ARG_POST, postId);
        this.startActivityForResult(intent, REQUEST_COMMENT);
    }

}
