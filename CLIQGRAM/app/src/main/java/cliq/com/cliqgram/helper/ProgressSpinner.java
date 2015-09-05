package cliq.com.cliqgram.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by litaoshen on 4/09/2015.
 */
public class ProgressSpinner {

    private static ProgressSpinner instance;
    private ProgressDialog progressDialog;

    public static ProgressSpinner getInstance() {
        if (instance == null) {
            instance = new ProgressSpinner();
        }

        return instance;
    }

    public void showSpinner(Context context, String message) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        instance.setTimeout(context, 60000);
        progressDialog.show();

    }


    public void dismissSpinner() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void setTimeout(final Context ctx, long time) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();

                Toast.makeText(ctx, "Log in timeout.", Toast.LENGTH_SHORT).show();

            }
        }, time);
    }

    public ProgressDialog getSpinner() {
        return progressDialog;
    }
}
