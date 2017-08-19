package mahorad.com.wifeye.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import mahorad.com.wifeye.publisher.event.persistence.Chronograph;
import mahorad.com.wifeye.util.Constants;
import mahorad.com.wifeye.base.BaseService;
import mahorad.com.wifeye.engine.Engine;
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

    private static final ReplaySubject<Boolean> source = ReplaySubject.createWithSize(1);

    @Inject
    Engine engine;

    @Inject
    Chronograph chronograph;

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
        started = true;
        chronograph.start();
        engine.start();
        Timber.tag(TAG).v("started main service");
        source.onNext(true);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (!started) return;
        started = false;
        chronograph.stop();
        engine.stop();
        Timber.tag(TAG).v("stopped main service");
        source.onNext(false);
    }

    public static Flowable<Boolean> stateChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.newThread());
    }
}