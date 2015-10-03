package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.fragments.CommentFragment;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class CommentActivity extends AppCompatActivity {

    public static final String ARG_POST = "post";

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // inject views
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String postId = intent.getStringExtra(ARG_POST);

        Fragment fragment = CommentFragment.newInstance(postId);
        fm.beginTransaction()
                .replace(R.id.container_body, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
        this.finish();
    }
}
