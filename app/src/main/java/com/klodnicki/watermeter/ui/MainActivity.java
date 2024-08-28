package com.klodnicki.watermeter.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.klodnicki.watermeter.R;
import com.klodnicki.watermeter.api.ApiClient;
import com.klodnicki.watermeter.api.ApiService;
import com.klodnicki.watermeter.command.CommandAdapter;
import com.klodnicki.watermeter.model.PermissionsResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private RecyclerView recyclerView;
    private CommandAdapter commandAdapter;
    private Button logoutButton;
    private Button fetchPermissionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commandAdapter = new CommandAdapter();
        recyclerView.setAdapter(commandAdapter);

        logoutButton = findViewById(R.id.logoutButton);
        fetchPermissionsButton = findViewById(R.id.fetchPermissionsButton);

        logoutButton.setOnClickListener(v -> performLogout());
        fetchPermissionsButton.setOnClickListener(v -> fetchPermissions());

        if (arePermissionsGranted()) {
            fetchPermissions();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean arePermissionsGranted() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void fetchPermissions() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(this, "No token found, please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("MainActivity", "Using token: " + token);

        ApiService apiService = ApiClient.getApiService();
        Call<PermissionsResponse> call = apiService.getPermissions(token);

        call.enqueue(new Callback<PermissionsResponse>() {
            @Override
            public void onResponse(Call<PermissionsResponse> call, Response<PermissionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handlePermissions(response.body());
                } else {
                    Log.e("MainActivity", "Failed to fetch permissions. Response code: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to fetch permissions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PermissionsResponse> call, Throwable t) {
                Log.e("MainActivity", "Error fetching permissions: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error fetching permissions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePermissions(PermissionsResponse permissionsResponse) {
        commandAdapter.setGroups(permissionsResponse.getGroups());
    }

    private void performLogout () {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("auth_token");
        editor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
