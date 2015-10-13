package cliq.com.cliqgram.callbacks;

import android.bluetooth.BluetoothSocket;

/**
 * Created by Benjamin on 15/10/10.
 */
public interface SocketSetupCallback {
    void onSocketSetup(BluetoothSocket socket);
}
