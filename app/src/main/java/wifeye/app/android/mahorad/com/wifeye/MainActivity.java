package wifeye.app.android.mahorad.com.wifeye;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.consumers.BaseConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.BasePersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;

public class MainActivity extends ActivityManagePermission {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText contents;

    private final IPersistence persistence = new BasePersistence();

    private BssidNamePublisher bssidNamePublisher;

    private CellTowerPublisher cellTowerPublisher;

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
    }

    private void handleCoarseLocationPermission() {
        String[] permissionRequests = {
                PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE
        };
        askCompactPermissions(permissionRequests, new PermissionResult() {
            @Override
            public void permissionGranted() { setup();}
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() { }
        });
    }

    private void setup() {
        Log.d(TAG, "setting up messaging");
        if (bssidNamePublisher != null)
            return;
        createPublishers();
        subscribeConsumer();
        startPublishers();
    }

    private void createPublishers() {
        final Context context = getApplicationContext();
        bssidNamePublisher = new BssidNamePublisher(context);
        cellTowerPublisher = new CellTowerPublisher(context, persistence);
    }

    private void subscribeConsumer() {
        final Context context = getApplicationContext();
        BaseConsumer consumer = BaseConsumer.build(context, persistence);
        bssidNamePublisher.subscribe(consumer);
        cellTowerPublisher.subscribe(consumer);
    }

    private void startPublishers() {
        bssidNamePublisher.start();
        cellTowerPublisher.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}