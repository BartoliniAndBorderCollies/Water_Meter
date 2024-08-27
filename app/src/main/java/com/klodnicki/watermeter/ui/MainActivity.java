package com.klodnicki.watermeter.ui;

import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.klodnicki.watermeter.R;
import com.klodnicki.watermeter.bluetooth.BluetoothService;
import com.klodnicki.watermeter.command.CommandAdapter;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private RecyclerView recyclerView;
    private CommandAdapter commandAdapter;
    private Button logoutButton;
    private Button fetchPermissionsButton;
    private BluetoothService bluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize BluetoothService
        bluetoothService = new BluetoothService();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commandAdapter = new CommandAdapter();
        recyclerView.setAdapter(commandAdapter);

        logoutButton = findViewById(R.id.logoutButton);
        fetchPermissionsButton = findViewById(R.id.fetchPermissionsButton);


}
