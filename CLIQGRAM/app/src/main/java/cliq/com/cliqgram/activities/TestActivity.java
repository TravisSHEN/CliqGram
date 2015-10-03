package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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




    @OnClick(R.id.button_camera_test)
    public void goCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

}
