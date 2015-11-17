package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.SignupFailEvent;
import cliq.com.cliqgram.events.SignupSuccessEvent;
import cliq.com.cliqgram.helper.KeyboardManager;
import cliq.com.cliqgram.helper.NetworkConnection;
import cliq.com.cliqgram.helper.ProgressSpinner;
import cliq.com.cliqgram.helper.ToolbarModel;
import cliq.com.cliqgram.app.AppStarter;
import cliq.com.cliqgram.services.SignupService;
import de.greenrobot.event.Subscribe;

public class SignupActivity extends AppCompatActivity {

    // inject views by ButterKnife
    @Bind(R.id.text_signup_email)
    EditText emailText;
    @Bind(R.id.text_signup_username)
    EditText usernameText;
    @Bind(R.id.text_signup_password)
    EditText passwordText;
    @Bind(R.id.btn_signup_submit)
    Button submitBtn;
    @Bind(R.id.btn_signup_login)
    TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // inject views
        ButterKnife.bind(this);

        // setup toolbar
        ToolbarModel.setupToolbar(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        // register with EventBus
        AppStarter.eventBus.register(this);

    }

    @Override
    public void onStop() {
        AppStarter.eventBus.unregister(this);
        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

                // dismiss progress dialog when received response from signup
                // service
                ProgressSpinner.getInstance().dismissSpinner();

                if (baseEvent instanceof SignupSuccessEvent) {
                    // Back to log in activity
                    // with new registered username and password
                    Intent intent = new Intent();
                    intent.putExtra("username",
                            ((SignupSuccessEvent) baseEvent).getUsername());
                    intent.putExtra("password",
                            ((SignupSuccessEvent) baseEvent).getPassword());
                    setResult(RESULT_OK, intent);
                    SignupActivity.this.finish();

                } else if (baseEvent instanceof SignupFailEvent) {
                    // display failure message
                    Toast.makeText(SignupActivity.this, baseEvent.getMessage(),
                            Toast
                            .LENGTH_LONG).show();
                    Snackbar.make(submitBtn, baseEvent.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        }, 3000);

    }

    @OnClick({R.id.btn_signup_submit, R.id.btn_signup_login})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_signup_submit:

                // sign up
                signup();
                break;
            case R.id.btn_signup_login:

                // back to login activity directly
                SignupActivity.this.finish();
                break;
            default:
                break;
        }

        KeyboardManager.closeKeyboard(this);
    }


    private void signup() {

        if (NetworkConnection.isNetworkConnected(this)) {
            // assign values to attributes of instance of model
            String email = emailText.getText().toString().trim();
            String username = usernameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            ProgressSpinner.getInstance().showSpinner(this, "Registering...");
            SignupService.signup(this, username, password, email);
        } else {
            NetworkConnection.showAlert(this, loginBtn);
        }
    }

}

