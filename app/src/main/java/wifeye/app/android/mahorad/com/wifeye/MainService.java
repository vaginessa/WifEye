package wifeye.app.android.mahorad.com.wifeye;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import wifeye.app.android.mahorad.com.wifeye.constants.Constants;

public class MainService extends Service {

    private static final String TAG = MainServiceBinder.class.getSimpleName();

    class MainServiceBinder extends Binder {
        public MainServiceBinder getService() {
            return MainServiceBinder.this;
        }
    }

    private final IBinder binder = new MainServiceBinder();
    private ResultReceiver resultReceiver;

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

        MainApplication
                .mainComponent()
                .ctidPublisher()
                .start();

        MainApplication
                .mainComponent()
                .ssidPublisher()
                .start();

        started = true;
        Log.v(TAG, "started main service");
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (!started) return;

        MainApplication.mainComponent()
                .ctidPublisher()
                .stop();

        MainApplication.mainComponent()
                .ssidPublisher()
                .stop();

        started = false;
        Log.v(TAG, "stopped main service");
    }

}