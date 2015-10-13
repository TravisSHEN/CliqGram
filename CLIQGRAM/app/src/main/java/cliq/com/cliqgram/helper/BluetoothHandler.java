package cliq.com.cliqgram.helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import cliq.com.cliqgram.callbacks.IdReceivedCallback;
import cliq.com.cliqgram.callbacks.SocketSetupCallback;
import cliq.com.cliqgram.exceptions.BluetoothNotSupportedException;
import cliq.com.cliqgram.exceptions.BluetoothOffException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Benjamin on 15/10/10.
 */
public class BluetoothHandler {
    public static final UUID MY_UUID = UUID.fromString("5753723d-fa31-4b00-8206-2d5a318f6382");
    public static final String NAME = "CLIQGRAM";
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<String> deviceList = new ArrayList<>();
    private Context mContext;
    private String message;
    private List<ConnectThread> connectThreadList = new ArrayList<>();
    private AcceptThread mAcceptThread;
    private IdReceivedCallback mIdReceivedCallback;
    private SocketSetupCallback serverSetupCallback = new SocketSetupCallback() {
        @Override
        public void onSocketSetup(BluetoothSocket socket) {
            Thread serverThread = new ConnectedThread(socket, mIdReceivedCallback);
            serverThread.start();
        }
    };

    public BluetoothHandler(Context mContext, IdReceivedCallback idReceivedCallback) {
        this.mContext = mContext;
        this.mIdReceivedCallback = idReceivedCallback;
    }

    public boolean sendMessage(String msg) throws BluetoothNotSupportedException, BluetoothOffException {
        // TODO
        checkBluetoothState();
        message = msg;
        acquireDevices();
        if (deviceList.size() > 0) {
            for (String deviceInfo : deviceList) {
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceInfo.split("\\n")[1]);
                SocketSetupCallback clientSetupCallback = new SocketSetupCallback() {
                    @Override
                    public void onSocketSetup(BluetoothSocket socket) {
                        ConnectedThread clientThread = new ConnectedThread(socket, null);
                        clientThread.write(message.getBytes());
                    }
                };
                ConnectThread connectThread = new ConnectThread(device, clientSetupCallback);
                connectThread.start();
                connectThreadList.add(connectThread);
            }
            return true;
        }
        return false;
    }

    public void cancelInProgressConnetions() {
        // TODO
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
        }
        if (connectThreadList.size() > 0) {
            for (ConnectThread thread : connectThreadList) {
                if (thread != null) {
                    thread.cancel();
                }
            }
        }
    }

    private void acquireDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                deviceList.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    public void enableDiscoverability() throws BluetoothNotSupportedException, BluetoothOffException {
        checkBluetoothState();
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        mContext.startActivity(discoverableIntent);
        mAcceptThread = new AcceptThread(serverSetupCallback);
        mAcceptThread.start();
    }

    private void checkBluetoothState() throws BluetoothNotSupportedException, BluetoothOffException {
        if (mBluetoothAdapter == null) {
            throw new BluetoothNotSupportedException("Bluetooth not supported.");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            throw new BluetoothOffException("Bluetooth is off.");
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private IdReceivedCallback mIdReceivedCallback;

        public ConnectedThread(BluetoothSocket socket, IdReceivedCallback idReceivedCallback) {
            mmSocket = socket;
            this.mIdReceivedCallback = idReceivedCallback;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while ((bytes = mmInStream.read(buffer)) != -1) {
                    // Send the obtained bytes to the UI activity
                    byteArrayOutputStream.write(buffer, 0, bytes);
                }
                mIdReceivedCallback.onIdReceived(new String(byteArrayOutputStream.toByteArray()).trim());
                byteArrayOutputStream.close();
                cancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                cancel();
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmOutStream.close();
                mmInStream.close();
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private SocketSetupCallback mSocketSetupCallback;

        private AcceptThread(SocketSetupCallback socketSetupCallback) {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
            this.mSocketSetupCallback = socketSetupCallback;
        }

        public void run() {
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    mSocketSetupCallback.onSocketSetup(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private SocketSetupCallback mSocketSetupCallback;

        private ConnectThread(BluetoothDevice device, SocketSetupCallback socketSetupCallback) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
            this.mSocketSetupCallback = socketSetupCallback;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            mSocketSetupCallback.onSocketSetup(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
