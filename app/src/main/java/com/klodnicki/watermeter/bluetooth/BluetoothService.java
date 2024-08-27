package com.klodnicki.watermeter.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {

    private final BluetoothAdapter bluetoothAdapter; //manages the device's bluetooth adapter
    private BluetoothSocket bluetoothSocket; //represents the connection to the device
    private OutputStream outputStream; //used to send data

    private static final UUID MY_UUID = UUID.fromString("UUID-HERE"); //unique identifier for the bluetooth connection
    // TODO: To be replaced with actual UUID

    public BluetoothService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private boolean hasBluetoothPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public void connect(BluetoothDevice device, Context context) throws IOException {
        if (!hasBluetoothPermissions(context)) {
            throw new SecurityException("Bluetooth permissions are not granted");
        }

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (SecurityException e) {
            //TODO: Handle the SecurityException
            e.printStackTrace();
            throw new IOException("Failed to connect due to missing permissions", e);
        }
    }

    public void sendData(String data, Context context) throws IOException {
        if (!hasBluetoothPermissions(context)) {
            throw new SecurityException("Bluetooth permissions are not granted");
        }

        if (outputStream != null) {
            outputStream.write(data.getBytes());
        } else {
            throw new IOException("Bluetooth output stream is not available");
        }
    }

    public void close() throws IOException {
        if (bluetoothSocket != null) {
            bluetoothSocket.close();
        }
    }


}
