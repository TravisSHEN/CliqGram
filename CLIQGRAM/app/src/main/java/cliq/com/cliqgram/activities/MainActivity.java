package cliq.com.cliqgram.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.fragments.ActivityFragment;
import cliq.com.cliqgram.fragments.FeedFragment;
import cliq.com.cliqgram.fragments.ProfileFragment;
import cliq.com.cliqgram.fragments.SettingFragment;
import cliq.com.cliqgram.helper.Utils;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.ToolbarModel;
import cliq.com.cliqgram.services.PostService;

import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


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
        post = (Button) findViewById(R.id.bPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICKED_IMG);*/
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        PICKED_IMG);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKED_IMG && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            //String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Date now = new Date();
            String date = Utils.dateFormat.format(now);
            //byte[] imageData = convertImageToByte(selectedImage);
            byte[] imageData = null;
            try {
                imageData = getBytes(selectedImage);
            }catch(IOException e){

            }
            Post post = new Post(imageData, "my new photo", "Melbourne",
                    ParseUser.getCurrentUser(), null, date, null);
            PostService.post(post);
        }
    }
    private byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
    public byte[] getBytes(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
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
    boolean onLongClick() {
        Toast.makeText(this, "Long click", Toast.LENGTH_SHORT)
                .show();
        return true;
    }

    @OnClick(R.id.btn_float_action)
    void onClick() {
        Toast.makeText(this, "Short click", Toast.LENGTH_SHORT)
                .show();
    }
}
