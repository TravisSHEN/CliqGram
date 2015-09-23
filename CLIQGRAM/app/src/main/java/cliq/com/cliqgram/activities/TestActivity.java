package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;

public class TestActivity extends ActionBarActivity {

    @Bind(R.id.button_camera_test)
    Button buttonCameraTest;
    private String logTag = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

//        Log.d(logTag, (buttonCameraTest == null ? "Null" : "Not Null"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_camera_test)
    public void goCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

}
