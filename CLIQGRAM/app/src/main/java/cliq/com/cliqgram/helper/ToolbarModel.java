package cliq.com.cliqgram.helper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cliq.com.cliqgram.R;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class ToolbarModel {

    public static void setupToolbar(AppCompatActivity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
