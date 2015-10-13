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
import cliq.com.cliqgram.helper.BluetoothHelper;

public class TestActivity extends ActionBarActivity {
    @Bind(R.id.button_test_send)
    Button buttonSend;
    @Bind(R.id.button_test_receive)
    Button buttonReceive;
    @Bind(R.id.textView_test)
    TextView textView;
    @Bind(R.id.button_test_cancel)
    Button buttonCancel;


    private IdReceivedCallback mIdReceivedCallback = new IdReceivedCallback() {
        @Override
        public synchronized void onIdReceived(String id) {
            textView.setText(id);
        }
    };

    private BluetoothHelper mBluetoothHelper = new BluetoothHelper(this, mIdReceivedCallback);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.button_test_receive)
    public void receive () {
        try {
            mBluetoothHelper.startBluetooth();
        } catch (BluetoothOffException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.button_test_cancel)
    public void cancel() {
    }

    @OnClick(R.id.button_test_send)
    public void send() {
        mBluetoothHelper.sendMessage("test");
    }


}
