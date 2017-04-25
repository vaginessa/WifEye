package wifeye.app.android.mahorad.com.wifeye;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.consumers.BaseConsumer;
import wifeye.app.android.mahorad.com.wifeye.engine.Engine;
import wifeye.app.android.mahorad.com.wifeye.persist.BasePersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;
import wifeye.app.android.mahorad.com.wifeye.wifi.AndroidWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.IWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.WifiController;

public class MainActivity extends ActivityManagePermission {

    private static final int UPDATE_DEVICE_STATS = 0;
    private EditText contents;

    private final IPersistence persistence = new BasePersistence();

    private BssidNamePublisher bssidNamePublisher;

    private CellTowerPublisher cellTowerPublisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contents = (EditText) findViewById(R.id.contents);

        askCompactPermission(PermissionUtils.Manifest_ACCESS_COARSE_LOCATION, new PermissionResult() {
            @Override
            public void permissionGranted() {
                //permission granted
                //replace with your action
            }

            @Override
            public void permissionDenied() {
                //permission denied
                //replace with your action
            }
            @Override
            public void permissionForeverDenied() {
                // user has check never ask again
                // you need to open setting manually
                //  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //  Uri uri = Uri.fromParts("package", getPackageName(), null);
                //   intent.setData(uri);
                //  startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            }
        });


        setupMessaging();
    }

    private void setupMessaging() {
        final Context context = getApplicationContext();
        bssidNamePublisher = new BssidNamePublisher(context);
        cellTowerPublisher = new CellTowerPublisher(context, persistence);

        BaseConsumer consumer = createConsumer();

        bssidNamePublisher.subscribe(consumer);
        cellTowerPublisher.subscribe(consumer);

        bssidNamePublisher.start();
        cellTowerPublisher.start();
    }

    @NonNull
    private BaseConsumer createConsumer() {
        Context context = getApplicationContext();
        Object systemService = context.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) systemService;
        IWifiHandler androidWifiHandler = new AndroidWifiHandler(wifiManager);
        WifiController wifiController = new WifiController(androidWifiHandler);
        Engine engine = new Engine(wifiController, persistence);
        return new BaseConsumer(engine);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}