package wifeye.app.android.mahorad.com.wifeye.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import wifeye.app.android.mahorad.com.wifeye.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.consumers.EventConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.BasePersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.publishers.state.IState;
import wifeye.app.android.mahorad.com.wifeye.wifi.AndroidWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.IWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.Wifi;

public class MainService extends Service implements IStateConsumer {

    private static final String TAG = MainServiceBinder.class.getSimpleName();

    class MainServiceBinder extends Binder {
        public MainServiceBinder getService() {
            return MainServiceBinder.this;
        }
    }

    private final IBinder binder = new MainServiceBinder();
    private ResultReceiver resultReceiver;

    private final IPersistence persistence = new BasePersistence();
    private BssidNamePublisher bssidNamePublisher;
    private CellTowerPublisher cellTowerPublisher;
    private EventConsumer eventConsumer;
    private boolean started;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        resultReceiver = intent.getParcelableExtra(Constants.PARCELABLE_RESULT_RECEIVER);
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return START_STICKY;
    }

    private void start() {
        if (started) return;
        createPublishers();
        subscribeConsumer();
        startPublishers();
        started = true;
        Log.v(TAG, "started main service");
    }

    private void createPublishers() {
        final Context context = getApplicationContext();
        bssidNamePublisher = new BssidNamePublisher(context);
        cellTowerPublisher = new CellTowerPublisher(context, persistence);
    }

    private void subscribeConsumer() {
        final Context context = getApplicationContext();
        eventConsumer = build(context, persistence);
        bssidNamePublisher.subscribe(eventConsumer);
        cellTowerPublisher.subscribe(eventConsumer);
    }

    private void startPublishers() {
        bssidNamePublisher.start();
        cellTowerPublisher.start();
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (!started) return;
        stopPublishers();
        unsubscribeConsumer();
        deletePublishers();
        started = false;
        Log.v(TAG, "stopped main service");
    }

    private void stopPublishers() {
        bssidNamePublisher.stop();
        cellTowerPublisher.stop();
    }

    private void unsubscribeConsumer() {
        bssidNamePublisher.subscribe(eventConsumer);
        cellTowerPublisher.subscribe(eventConsumer);
        eventConsumer = null;
    }

    private void deletePublishers() {
        bssidNamePublisher = null;
        cellTowerPublisher = null;
    }

    private EventConsumer build(Context context, IPersistence persistence) {
        if (context == null)
            throw new IllegalArgumentException();
        if (persistence == null)
            throw new IllegalArgumentException();
        Object systemService = context.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) systemService;
        IWifiHandler androidWifiHandler = new AndroidWifiHandler(wifiManager);
        Wifi wifi = new Wifi(androidWifiHandler);
        Engine engine = new Engine(wifi, persistence);
        engine.subscribe(this);
        return new EventConsumer(engine);
    }

    @Override
    public void onStateChanged(IState state) {
        // broadcast state changed
    }

}