package cliq.com.cliqgram.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.services.SignupService;

public class SignupActivity extends Activity {

    EditText emailText, usernameText, passwordText;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // initialize veiws first
        this.initializeViews();

        // register submit button with listener
        submitBtn.setOnClickListener(submitButtonListener);

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

    private View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // assign values to attributes of instance of model
            String email = emailText.getText().toString();
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            SignupService.signup(SignupActivity.this, username, password, email);
        }
    };

    private void initializeViews() {

        emailText = (EditText) findViewById(R.id.text_signup_email);
        usernameText = (EditText) findViewById(R.id.text_signup_username);
        passwordText = (EditText) findViewById(R.id.text_signup_password);

        submitBtn = (Button) findViewById(R.id.btn_signup_submit);

    }
}
