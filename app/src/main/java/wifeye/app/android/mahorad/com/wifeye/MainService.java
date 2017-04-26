package wifeye.app.android.mahorad.com.wifeye;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import wifeye.app.android.mahorad.com.wifeye.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.consumers.EventConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.BasePersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;

public class MainService extends Service {

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
    private EventConsumer consumer;

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
        if (bssidNamePublisher != null)
            return;
        createPublishers();
        subscribeConsumer();
        startPublishers();
        Log.v(TAG, "started main service");
    }

    private void createPublishers() {
        final Context context = getApplicationContext();
        bssidNamePublisher = new BssidNamePublisher(context);
        cellTowerPublisher = new CellTowerPublisher(context, persistence);
    }

    private void subscribeConsumer() {
        final Context context = getApplicationContext();
        consumer = EventConsumer.build(context, persistence);
        bssidNamePublisher.subscribe(consumer);
        cellTowerPublisher.subscribe(consumer);
    }

    private void startPublishers() {
        bssidNamePublisher.start();
        cellTowerPublisher.start();
    }

    @Override
    public void onDestroy() {
        stopPublishers();
        unsubscribeConsumer();
        deletePublishers();
        Log.v(TAG, "stopped main service");
    }

    private void stopPublishers() {
        bssidNamePublisher.stop();
        cellTowerPublisher.stop();
    }

    private void unsubscribeConsumer() {
        bssidNamePublisher.subscribe(consumer);
        cellTowerPublisher.subscribe(consumer);
        consumer = null;
    }

    private void deletePublishers() {
        bssidNamePublisher = null;
        cellTowerPublisher = null;
    }
}