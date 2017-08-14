package mahorad.com.wifeye.broadcast.manager.rx.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import mahorad.com.wifeye.broadcast.manager.rx.RxBroadcastReceiver;

import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;
import static dagger.internal.Preconditions.checkNotNull;

public final class RxWifiManager {

    private RxWifiManager() {
        throw new AssertionError("no instances");
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> wifiStateChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        return RxBroadcastReceiver
                .create(context, filter)
                .map(intent -> intent.getIntExtra(EXTRA_WIFI_STATE, -1));
    }

    @CheckResult
    @NonNull
    public static Observable<NetworkStateChangedEvent> networkStateChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        return RxBroadcastReceiver
                .create(context, filter)
                .map(intent -> {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
                    WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    return NetworkStateChangedEvent.create(networkInfo, bssid, wifiInfo);
                });
    }

    @CheckResult
    @NonNull
    public static Observable<Boolean> supplicantConnectionChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        IntentFilter filter = new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        return RxBroadcastReceiver
                .create(context, filter)
                .map(intent -> intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false));
    }

    @CheckResult
    @NonNull
    public static Observable<SupplicantStateChangedEvent> supplicantStateChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        IntentFilter filter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        return RxBroadcastReceiver
                .create(context, filter)
                .map(intent -> {
                    SupplicantState newState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                    int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                    return SupplicantStateChangedEvent.create(newState, error);
                });
    }
}