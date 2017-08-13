package mahorad.com.wifeye.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import mahorad.com.wifeye.util.Constants;
import mahorad.com.wifeye.base.BaseService;
import mahorad.com.wifeye.engine.Engine;

public class EngineService extends BaseService {

    class EngineServiceBinder extends Binder {
        public EngineServiceBinder getService() {
            return EngineServiceBinder.this;
        }
    }

    private static final String TAG = EngineService.class.getSimpleName();

    private static boolean started;

    private static final ReplaySubject<Boolean> state = ReplaySubject.createWithSize(1);

    private final IBinder binder = new EngineServiceBinder();

    private ResultReceiver resultReceiver;

    @Inject
    Engine engine;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void injectDependencies() {
        component().inject(this);
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
        engine.start(this);
        started = true;
        Log.v(TAG, "started main service");
        state.onNext(true);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (!started) return;
        engine.stop();
        started = false;
        Log.v(TAG, "stopped main service");
        state.onNext(false);
    }

    public static Observable<Boolean> stateObservable() {
        return state;
    }
}