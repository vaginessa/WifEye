package wifeye.app.android.mahorad.com.wifeye.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.app.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Signal;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.state.StateMachine;

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

    @Inject Wifi wifi;
    @Inject Signal signal;
    @Inject Internet internet;
    @Inject StateMachine stateMachine;

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication
                .mainComponent()
                .inject(this);
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

        wifi.registerBroadcast();
        internet.registerBroadcast();
        signal.startListening();
        stateMachine.start();

        started = true;
        Log.v(TAG, "started main service");
        broadcastStartup();
    }

    private void broadcastStartup() {
        Intent intent = new Intent(Constants.INTENT_SERVICE_STATE);
        intent.putExtra(Constants.EXTRAS_SERVICE_STATE, true);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (!started) return;

        stateMachine.stop();
        wifi.unregisterBroadcast();
        internet.unregisterBroadcast();
        signal.stopListening();

        started = false;
        Log.v(TAG, "stopped main service");
        broadcastShutdown();
    }

    private void broadcastShutdown() {
        Intent intent = new Intent(Constants.INTENT_SERVICE_STATE);
        intent.putExtra(Constants.EXTRAS_SERVICE_STATE, false);
        sendBroadcast(intent);
    }

}