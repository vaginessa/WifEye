package wifeye.app.android.mahorad.com.wifeye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.services.MainService;

public class MainActivity extends ActivityManagePermission {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button startService;
    private Button stopService;
    private Button permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService = (Button) findViewById(R.id.startService);
        stopService = (Button) findViewById(R.id.stopService);
        permissions = (Button) findViewById(R.id.permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupButtons();
    }

    private void setupButtons() {
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { start(); }
        });
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { stop(); }
        });
        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { handlePermissions(); }
        });
    }

    private void handlePermissions() {
        handleCoarseLocationPermission();
        handleDiskReadWritePermissions();
    }

    private void handleCoarseLocationPermission() {
        String permission = PermissionUtils.Manifest_ACCESS_COARSE_LOCATION;
        askCompactPermission(permission, new PermissionResult() {
            @Override
            public void permissionGranted() { }
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() { }
        });
    }

    private void handleDiskReadWritePermissions() {
        String[] permissionRequests = {
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE
        };
        askCompactPermissions(permissionRequests, new PermissionResult() {
            @Override
            public void permissionGranted() { start(); }
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() { }
        });
    }

    /**
     * starting main service
     */
    private void start() {
        startService(new Intent(this, MainService.class));
    }

    /**
     * starting main service
     */
    private void stop() {
        stopService(new Intent(this, MainService.class));
    }


}