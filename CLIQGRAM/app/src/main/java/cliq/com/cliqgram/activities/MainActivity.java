package cliq.com.cliqgram.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.MainViewPageAdapter;
import cliq.com.cliqgram.events.OpenCommentEvent;
import cliq.com.cliqgram.fragments.ActivityFragment;
import cliq.com.cliqgram.fragments.FeedFragment;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.fragments.SearchFragment;
import cliq.com.cliqgram.fragments.SettingFragment;
import cliq.com.cliqgram.helper.ToolbarModel;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.Util;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAVIGATION_ITEM_ID = "navigationItemID";

    private static final int REQUEST_COMMENT = 0;

    public static final int TAB_FEED = 0;
    public static final int TAB_SEARCH = 1;
    public static final int TAB_CAMERA = 2;
    public static final int TAB_ACTIVITY = 3;
    public static final int TAB_PROFILE = 4;


    FragmentManager fm = getSupportFragmentManager();

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

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

        // TODO: uncomment these if don't need tab bar on bottom
         // set click listener to navigation view
//        navigationView.setNavigationItemSelectedListener(this);

        // initialize showing fragment
//        this.showInitialSelectedFragment(savedInstanceState);

        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R
                .string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // initialize tab bar layout
        initializeTabLayout();

        // TODO: select photo from gallery

        post = (Button) findViewById(R.id.bPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKED_IMG);

            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKED_IMG && resultCode == RESULT_OK  && data != null) {
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            Uri selectedImage = data.getData();
            byte[] imageData = Util.convertImageToByte(selectedImage, contentResolver);
            // TODO: pass current user to here to create a new Post
            User user = UserService.getCurrentUser();
            Post post = Post.createPost(imageData, user, "New photo");
        }
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        this.showFragment(menuItem);

        return true;
    }


    void initializeTabLayout(){
        MainViewPageAdapter mainViewPageAdapter = new MainViewPageAdapter(this, fm);
        viewPager.setAdapter(mainViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        float scale_factor = 0.7f;
        tabLayout.getTabAt(TAB_FEED).setIcon(Util.resizeDrawable(this,
                R.drawable.icon_home, scale_factor));
        tabLayout.getTabAt(TAB_SEARCH).setIcon(Util.resizeDrawable(this,R
                .drawable.icon_search, scale_factor));
//        tabLayout.getTabAt(TAB_CAMERA).setIcon(R.drawable.icon_camera);
        tabLayout.getTabAt(TAB_CAMERA).setCustomView(float_button);
        tabLayout.getTabAt(TAB_ACTIVITY).setIcon(Util.resizeDrawable(this,R
                .drawable.icon_star, scale_factor));
        tabLayout.getTabAt(TAB_PROFILE).setIcon(Util.resizeDrawable(this, R
                .drawable.icon_user, scale_factor));
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
            case R.id.navigation_item_search:
                fragment = SearchFragment.newInstance();
                title = getString(R.string.navigation_item_search);

                Snackbar.make(toolbar, "Search selected", Snackbar
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

            fm.beginTransaction()
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

//        StarterApplication.BUS.post(new FABLongClickEvent());
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

        String postId = openFragmentEvent.getPost().getObjectId();
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(CommentActivity.ARG_POST, postId);
        this.startActivityForResult(intent, REQUEST_COMMENT);
    }

}
