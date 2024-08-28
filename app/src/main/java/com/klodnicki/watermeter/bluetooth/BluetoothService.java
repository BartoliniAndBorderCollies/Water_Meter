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
    //unique identifier for the bluetooth connection
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter bluetoothAdapter; //manages the device's bluetooth adapter
    private BluetoothSocket bluetoothSocket; //represents the connection to the device
    private OutputStream outputStream; //used to send data
    private InputStream inputStream; //used to receive data


    public BluetoothService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // Connect to a Bluetooth device
    public void connect(String address, Context context) throws IOException {
        if (!hasBluetoothPermissions(context)) {
            throw new SecurityException("Bluetooth permissions are not granted");
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
        } catch (SecurityException se) {
            Log.e(TAG, "Bluetooth connection failed due to missing permissions", se);
            throw se; // Handle or re-throw SecurityException
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect to Bluetooth device", e);
            throw e; // Re-throw the IOException after logging it
        }
    }

    // Send data with checksum calculation
    public void sendData(String data, Context context) throws IOException {
        if (!hasBluetoothPermissions(context)) {
            throw new SecurityException("Bluetooth permissions are not granted");
        }

        // Calculate checksum for the data
        String checksum = ChecksumUtil.calculateChecksum(data);

        // Optionally, append the checksum to the data
        String dataWithChecksum = data + checksum;

        if (outputStream != null) {
            outputStream.write(dataWithChecksum.getBytes());
            Log.d(TAG, "Data sent: " + dataWithChecksum);
        } else {
            throw new IOException("Bluetooth output stream is not available");
        }
    }

    // Receive data from Bluetooth device
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

    // Check if Bluetooth permissions are granted
    private boolean hasBluetoothPermissions(Context context) {  // ADDED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


}
