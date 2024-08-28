package com.klodnicki.watermeter.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.klodnicki.watermeter.utils.ChecksumUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {

    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public BluetoothService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connect(String address, Context context) throws IOException {
        if (hasBluetoothPermissions(context)) {
            throw new SecurityException("Bluetooth permissions are not granted");
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
        } catch (SecurityException se) {
            Log.e(TAG, "Bluetooth connection failed due to missing permissions", se);
            throw se;
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect to Bluetooth device", e);
            throw e;
        }
    }

    public void sendData(String data, Context context) throws IOException {
        if (hasBluetoothPermissions(context)) {
            throw new SecurityException("Bluetooth permissions are not granted");
        }

        String checksum = ChecksumUtil.calculateChecksum(data);
        String dataWithChecksum = data + checksum;

        if (outputStream != null) {
            outputStream.write(dataWithChecksum.getBytes());
            Log.d(TAG, "Data sent: " + dataWithChecksum);
        } else {
            throw new IOException("Bluetooth output stream is not available");
        }
    }

    public String receiveData() throws IOException {
        byte[] buffer = new byte[1024];
        int bytes;
        if (inputStream != null) {
            bytes = inputStream.read(buffer);
            String receivedData = new String(buffer, 0, bytes);
            Log.d(TAG, "Data received: " + receivedData);
            return receivedData;
        } else {
            throw new IOException("Bluetooth input stream is not available");
        }
    }

    public void close() throws IOException {
        if (bluetoothSocket != null) {
            bluetoothSocket.close();
            Log.d(TAG, "Bluetooth connection closed.");
        }
    }

    private boolean hasBluetoothPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED;
        } else {
            return false;
        }
    }

}
