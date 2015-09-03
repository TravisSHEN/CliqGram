package cliq.com.cliqgram.activities;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.parse.ParseAnalytics;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessfulEvent;
import cliq.com.cliqgram.model.ToolbarModel;
import cliq.com.cliqgram.services.LoginService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText, passwordText;
    private Button loginBtn;
    private TextView signupBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // register this activity to eventbus
        EventBus.getDefault().register(this);

        this.initializeViews();

        // bind two buttons with onClickListener
        loginBtn.setOnClickListener(this.onClickListener);
        signupBtn.setOnClickListener(this.onClickListener);

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
                progressDialog.dismiss();

                if (baseEvent instanceof LoginSuccessfulEvent) {
                    // open main activity
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    LoginActivity.this.startActivity(intent);

                } else if (baseEvent instanceof LoginFailEvent) {
                    // display failure message
                    Snackbar.make(loginBtn, "Login failed - "
                            + baseEvent.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        }, 3000);


    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_login:

                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();

                    Toast.makeText(LoginActivity.this, "Trying to login...", Toast
                            .LENGTH_SHORT).show();

                    LoginService.authenticate(username, password);

                    showProgressDialog("Loging in...");

                    break;
                case R.id.btn_signup:
                    Intent intent = new Intent(LoginActivity.this,
                            SignupActivity.class);
                    startActivity(intent);
                    break;
                default:
                    Log.d("Login Activity", "Button cannot be found.");
            }
        }
    };

    private void initializeViews() {

        usernameText = (EditText) findViewById(R.id.text_username);
        passwordText = (EditText) findViewById(R.id.text_password);

        loginBtn = (Button) findViewById(R.id.btn_login);
        signupBtn = (TextView) findViewById(R.id.btn_signup);

    }

    private void showProgressDialog(String message) {

        progressDialog = new ProgressDialog
                (LoginActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

}
