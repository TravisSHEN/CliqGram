package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;

/**
 * Created by litaoshen on 5/10/2015.
 */
public class SearchAdapter extends CursorAdapter {

    List<ParseUser> userList;

    // text view for showing results
    @Bind(R.id.search_item)
    TextView textView;


    public SearchAdapter(Context context, MatrixCursor cursor, List<ParseUser>
            userList) {
        super(context, cursor, false);

        if (userList == null ){
            this.userList = new ArrayList<>();
        } else {
            this.userList = userList;
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater
                .inflate(R.layout.search_result_item, parent, false);

        // bind view to ButterKnife
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ParseUser user = userList.get(cursor.getPosition());
        if (user == null) {
            return;
        }

        textView.setText(user.getUsername());
    }
}
