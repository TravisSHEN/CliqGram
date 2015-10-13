package cliq.com.cliqgram.helper;

import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import cliq.com.cliqgram.callbacks.IdReceivedCallback;
import cliq.com.cliqgram.exceptions.BluetoothOffException;

import java.io.*;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Benjamin on 15/10/13.
 */
public class BluetoothHelper {
    public static final UUID MY_UUID = UUID.fromString("1246fb14-0d3c-442c-b4eb-3d44b9f49733");
    private final static int REQUEST_ENABLE_BT = 1;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                Log.d("Bluetooth Device:", name);
            }
        }
    };
    private IdReceivedCallback mIdReceivedCallback;
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private ServerThread myServer;
    private boolean isServer = true;

    public BluetoothHelper(Context mContext, IdReceivedCallback mIdReceivedCallback) {
        this.mContext = mContext;
        this.mIdReceivedCallback = mIdReceivedCallback;
    }

    public void startBluetooth() throws BluetoothOffException {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            throw new BluetoothOffException("Bluetooth Off");
        }

        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        mContext.startActivity(discoverableIntent);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);

        myServer = new ServerThread(mIdReceivedCallback);
        myServer.start();


    }

    public void sendMessage(String msg) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                isServer = false;
                ConnectThread myConnection = new ConnectThread(device, msg);
                myConnection.start();
                String deviceName = device.getName();
                Log.d("Bluetooth Device:", deviceName);
            }
        }
    }

    private class ServerThread extends Thread {
        private final BluetoothServerSocket myServSocket;
        private IdReceivedCallback mIdReceivedCallback;
        private StringBuilder idBuilder = new StringBuilder();

        public ServerThread(IdReceivedCallback idReceivedCallback) {
            mIdReceivedCallback = idReceivedCallback;
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServer", MY_UUID);
            } catch (IOException e) {
                Log.e("Bluetooth", "Server establishing failed");
            }

            myServSocket = tmp;
        }

        public void run() {
            Log.e("Bluetooth", "Begin waiting for connection");
            BluetoothSocket connectSocket = null;
            InputStream inStream = null;
            OutputStream outStream = null;
            String line = "";

            while (true) {
                try {
                    connectSocket = myServSocket.accept();
                    mBluetoothAdapter.cancelDiscovery();
                } catch (IOException e) {
                    Log.e("Bluetooth", "Connection failed");
                    break;
                }

                try {
                    inStream = connectSocket.getInputStream();
                    outStream = connectSocket.getOutputStream();
                    int i = 0;

                    while (i != -1) {
                        i = inStream.read();
                        idBuilder.append((char)i);
                    }
                } catch (IOException e) {
                    Log.e("Bluetooth", idBuilder.toString());
                    mIdReceivedCallback.onIdReceived(idBuilder.toString());
                    try {
                        inStream.close();
                        outStream.close();
                        connectSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }

            }

            try {
                if (inStream != null && outStream != null) {
                    inStream.close();
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mySocket;
        private String message;
        private BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device, String msg) {
            message = msg;
            mDevice = device;
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e("Bluetooth", "Could not connect");
            }
            mySocket = tmp;
        }

        public void run() {
            InputStream inStream = null;
            OutputStream outStream = null;
            mBluetoothAdapter.cancelDiscovery();

            try {
                mySocket.connect();
            } catch (IOException e) {
                Log.e("Bluetooth", mDevice.getName() + ": Could not establish connection with device");
                try {
                    mySocket.close();
                } catch (IOException e1) {
                    Log.e("Bluetooth", mDevice.getName() + ": could not close socket", e1);
                }
            }

            String line = "";
            try {
                inStream = mySocket.getInputStream();
                outStream = mySocket.getOutputStream();

                outStream.write(message.getBytes());
                outStream.flush();
                Log.e("Bluetooth", "Message sent: " + message);
                inStream.close();
                outStream.close();
                cancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mySocket.close();
            } catch (IOException e) {
                Log.e("Bluetooth", mDevice.getName() + ": Could not close socket");
            }
        }
    }
}
