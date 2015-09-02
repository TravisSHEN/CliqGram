package cliq.com.cliqgram.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import cliq.com.cliqgram.services.LoginService;

public class LoginActivity extends Activity {

    EditText usernameText, passwordText;
    Button loginBtn;
    TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.initializeViews();

        loginBtn.setOnClickListener(this.onClickListener);

        signupBtn.setOnClickListener(this.onClickListener);


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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_login:

                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();

                    Toast.makeText(LoginActivity.this, "Trying to login...", Toast
                            .LENGTH_SHORT).show();

                    LoginService.authenticate(LoginActivity.this, username, password);
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
}
