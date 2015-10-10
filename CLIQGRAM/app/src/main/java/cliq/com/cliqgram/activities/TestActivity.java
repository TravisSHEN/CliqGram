package cliq.com.cliqgram.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.callbacks.IdReceivedCallback;
import cliq.com.cliqgram.exceptions.BluetoothNotSupportedException;
import cliq.com.cliqgram.exceptions.BluetoothOffException;
import cliq.com.cliqgram.helper.BluetoothHandler;

public class TestActivity extends ActionBarActivity {
    @Bind(R.id.button_test_send)
    Button buttonSend;
    @Bind(R.id.button_test_receive)
    Button buttonReceive;
    @Bind(R.id.textView_test)
    TextView textView;
    @Bind(R.id.button_test_cancel)
    Button buttonCancel;

    private BluetoothHandler mBluetoothHandle;
    private IdReceivedCallback mIdReceivedCallback = new IdReceivedCallback() {
        @Override
        public void onIdReceived(String id) {
            textView.setText(id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);
        mBluetoothHandle = new BluetoothHandler(this, mIdReceivedCallback);
    }

    @OnClick(R.id.button_test_receive)
    public void receive () {
        try {
            mBluetoothHandle.enableDiscoverability();
        } catch (BluetoothNotSupportedException e) {
            e.printStackTrace();
        } catch (BluetoothOffException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.button_test_cancel)
    public void cancel() {
        mBluetoothHandle.cancelInProgressConnetions();
    }

    @OnClick(R.id.button_test_send)
    public void send() {
        try {
            mBluetoothHandle.sendMessage("test");
        } catch (BluetoothNotSupportedException e) {
            e.printStackTrace();
        } catch (BluetoothOffException e) {
            e.printStackTrace();
        }
    }


}
