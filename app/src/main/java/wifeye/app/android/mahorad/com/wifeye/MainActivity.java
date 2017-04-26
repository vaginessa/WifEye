package wifeye.app.android.mahorad.com.wifeye;

import android.os.Bundle;
import android.widget.EditText;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class MainActivity extends ActivityManagePermission {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contents = (EditText) findViewById(R.id.contents);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handlePermissions();
    }

    private void handlePermissions() {
        handleCoarseLocationPermission();
        handleDiskReadWritePermissions();
    }

    private void handleCoarseLocationPermission() {
        String permission = PermissionUtils.Manifest_ACCESS_COARSE_LOCATION;
        askCompactPermission(permission, new PermissionResult() {
            @Override
            public void permissionGranted() { start(); }
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() {}
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

    private void start() {
        // TODO start main service
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}