package cliq.com.cliqgram.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.fragments.ActivityFragment;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.fragments.SettingFragment;
import cliq.com.cliqgram.fragments.UserFeedFragment;
import cliq.com.cliqgram.model.ToolbarModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.mDrawer)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // inject views
        ButterKnife.bind(this);

        // setup toolbar
        ToolbarModel.setupToolbar(this);

        // set click listener to navigation view
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        // close drawer when select one item
        mDrawerLayout.closeDrawers();
        menuItem.setChecked(true);

        // open corresponding fragment
        Fragment fragment = null;
        String title = "";

        switch (menuItem.getItemId()) {
            case R.id.navigation_item_home:

                fragment = new UserFeedFragment();
                title = getString(R.string.navigation_item_home);

                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_item_profile:

                fragment = new ProfileFragment();
                title = getString(R.string.navigation_item_profile);
                break;
            case R.id.navigation_item_activity:

                fragment = new ActivityFragment();
                title = getString(R.string.navigation_item_activity);
                break;
            case R.id.navigation_item_setting:

                fragment = new SettingFragment();
                title = getString(R.string.navigation_item_setting);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
        return false;
    }
}
