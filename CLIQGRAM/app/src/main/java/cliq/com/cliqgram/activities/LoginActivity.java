package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseAnalytics;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.StarterApplication;
import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessEvent;
import cliq.com.cliqgram.helper.NetworkConnection;
import cliq.com.cliqgram.helper.ProgressSpinner;
import cliq.com.cliqgram.helper.ToolbarModel;
import cliq.com.cliqgram.services.LoginService;
import de.greenrobot.event.Subscribe;

public class LoginActivity extends AppCompatActivity {

    // Request signup activity
    private static int REQUEST_SIGNUP = 0;

    // inject views by ButterKnife
    @Bind(R.id.text_username)
    EditText usernameText;
    @Bind(R.id.text_password)
    EditText passwordText;
    @Bind(R.id.btn_login)
    Button loginBtn;
    @Bind(R.id.btn_signup)
    TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // inject views
        ButterKnife.bind(this);

        // register this activity to eventbus
        StarterApplication.BUS.register(this);

        // setup action bar by using toolbar
        ToolbarModel.setupToolbar(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // if sign up successfully,
                // log in user automatically.
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                login(username, password);
            }
        }
    }

    /**
     * Subscribe to login event in cluding LoginFailEvent and
     * LoginSuccessfulEvent
     *
     * @param baseEvent
     */
    @Subscribe
    public void onEvent(final BaseEvent baseEvent) {

        // Delay to perform further operations
        // to get better animation for progress bar
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // dismiss progress dialog when received response from login service
                ProgressSpinner.getInstance().dismissSpinner();

                if (baseEvent instanceof LoginSuccessEvent) {
                    // open main activity
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    LoginActivity.this.startActivity(intent);

                } else if (baseEvent instanceof LoginFailEvent) {
                    // display failure message
                    Snackbar.make(loginBtn, baseEvent.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        }, 3000);

    }


    @OnClick({R.id.btn_login, R.id.btn_signup})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                login(username, password);

                break;
            case R.id.btn_signup:
                Intent intent = new Intent(LoginActivity.this,
                        SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                break;
            default:
                Log.e("Login Activity", "Button cannot be found.");
        }
    }


    private void login(String username, String password) {

        if (NetworkConnection.isNetworkConnected(this)) {

//            showProgressDialog("Loging in...");
            ProgressSpinner.getInstance().showSpinner(this, "Loging in...");
            LoginService.authenticate(username, password);
        } else {
            NetworkConnection.showAlert(this, loginBtn);
        }
    }

}
