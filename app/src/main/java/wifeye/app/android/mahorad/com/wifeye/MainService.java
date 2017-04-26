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
import wifeye.app.android.mahorad.com.wifeye.consumers.BaseConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.BasePersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;

public class MainService extends Service {

    class MainServiceBinder extends Binder {
        public MainServiceBinder getService() {
            return MainServiceBinder.this;
        }
    }

    private static final String TAG = MainServiceBinder.class.getSimpleName();

    private final IBinder binder = new MainServiceBinder();
    private ResultReceiver resultReceiver;

    @Override
    public void onCreate() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        resultReceiver = intent.getParcelableExtra(Constants.PARCELABLE_RESULT_RECEIVER);
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
    }

    @Override
    public void onDestroy() {
    }


    /** -------------------------- */
    private final IPersistence persistence = new BasePersistence();
    private BssidNamePublisher bssidNamePublisher;
    private CellTowerPublisher cellTowerPublisher;

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

}