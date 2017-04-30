package wifeye.app.android.mahorad.com.wifeye;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.state.IState;
import wifeye.app.android.mahorad.com.wifeye.view.IMainView;

public class MainActivity extends ActivityManagePermission implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Presenter presenter = new Presenter(this);

    private TextView serviceState;
    private TextView deviceState;
    private TextView ssidTextView;
    private TextView lastConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUserInterface();
        presenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void setupUserInterface() {
        serviceState = (TextView) findViewById(R.id.serviceState);
        deviceState = (TextView) findViewById(R.id.deviceState);
        ssidTextView = (TextView) findViewById(R.id.ssid);
        lastConnect = (TextView) findViewById(R.id.lastConnect);
    }

    public void handlePermissions(View view) {
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
            public void permissionGranted() { }
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() { }
        });
    }

    public void startMainService(View view) {
        presenter.startMainService();
    }

    public void stopMainService(View view) {
        presenter.stopMainService();
    }

    @Override
    public void updateServiceState(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serviceState.setText(enabled ? "ENABLE" : "DISABLE");
            }
        });
    }

    @Override
    public void updateSsidNameInfo(final String ssid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ssidTextView.setText(ssid == null ? "" : ssid);
                lastConnect.setText(ssid == null ? "" : getDate());
            }
        });
    }

    private String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }

    @Override
    public void updateEngineState(final IState state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceState.setText(state.toString());
            }
        });
    }

}