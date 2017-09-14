package mahorad.com.wifeye.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import mahorad.com.wifeye.base.BaseService;
import mahorad.com.wifeye.engine.Engine;
import mahorad.com.wifeye.publisher.event.persistence.EventRegister;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.util.Constants;
import timber.log.Timber;

public class EngineService extends BaseService {

    private static final String TAG = EngineService.class.getSimpleName();

    class EngineServiceBinder extends Binder {
        public EngineServiceBinder getService() {
            return EngineServiceBinder.this;
        }
    }

    private final IBinder binder = new EngineServiceBinder();

    private ResultReceiver resultReceiver;

    private static boolean started;

    @Inject
    Engine engine;

    @Inject
    EventRegister eventRegister;

    @Override
    public void onCreate() {
        super.onCreate();
    }

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
        started = true;
        eventRegister.start();
        engine.start();
        Timber.tag(TAG).v("started main service");
        RxEngineServiceMonitor.notify(true);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (!started) return;
        started = false;
        eventRegister.stop();
        engine.stop();
        Timber.tag(TAG).v("stopped main service");
        RxEngineServiceMonitor.notify(false);
    }

}