package cliq.com.cliqgram.helper;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by litaoshen on 8/10/2015.
 */
public class KeyboardManager {
    public static void closeKeyboard(Context context) {
        View view = ((AppCompatActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService
                            (Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}