package wifeye.app.android.mahorad.com.wifeye.app.rx;

import android.app.Application;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.NetworkStateChangedEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.RxWifiManager;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.SupplicantStateChangedEvent;

import static android.net.wifi.WifiManager.ERROR_AUTHENTICATING;
import static android.net.wifi.WifiManager.EXTRA_NEW_STATE;
import static android.net.wifi.WifiManager.EXTRA_SUPPLICANT_ERROR;
import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;
import static android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;

@RunWith(RobolectricTestRunner.class) //
public class RxWifiManagerTest {

    @Test
    public void wifiStateChanges() {
        Application application = RuntimeEnvironment.application;

        TestObserver<Integer> o = RxWifiManager.wifiStateChanges(application).test();
        o.assertValues();

        Intent intent1 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_DISABLED);
        application.sendBroadcast(intent1);
        o.assertValues(WIFI_STATE_DISABLED);

        Intent intent2 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_UNKNOWN);
        application.sendBroadcast(intent2);
        o.assertValues(WIFI_STATE_DISABLED, WIFI_STATE_UNKNOWN);

        Intent intent3 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_DISABLED);
        application.sendBroadcast(intent3);
        o.assertValues(WIFI_STATE_DISABLED, WIFI_STATE_UNKNOWN, WIFI_STATE_DISABLED);
    }

    @SuppressWarnings("ResourceType")
    @Test
    public void networkStateChanges() throws IllegalAccessException, InstantiationException {
        Application application = RuntimeEnvironment.application;

        TestObserver<NetworkStateChangedEvent> o = RxWifiManager.networkStateChanges(application).test();
        o.assertValues();

        NetworkInfo networkInfo1 = NetworkInfo.class.newInstance();
        WifiInfo wifiInfo1 = WifiInfo.class.newInstance();
        Intent intent1 = new Intent(NETWORK_STATE_CHANGED_ACTION) //
                .putExtra(WifiManager.EXTRA_NETWORK_INFO, networkInfo1)
                .putExtra(WifiManager.EXTRA_BSSID, "foo")
                .putExtra(WifiManager.EXTRA_WIFI_INFO, wifiInfo1);
        application.sendBroadcast(intent1);
        NetworkStateChangedEvent event1 =
                NetworkStateChangedEvent.create(networkInfo1, "foo", wifiInfo1);
        o.assertValues(event1);

        NetworkInfo networkInfo2 = NetworkInfo.class.newInstance();
        Intent intent2 = new Intent(NETWORK_STATE_CHANGED_ACTION) //
                .putExtra(WifiManager.EXTRA_NETWORK_INFO, networkInfo2);
        application.sendBroadcast(intent2);
        NetworkStateChangedEvent event2 = NetworkStateChangedEvent.create(networkInfo2, null, null);
        o.assertValues(event1, event2);
    }

    @SuppressWarnings("ResourceType")
    @Test //
    public void supplicantStateChanges() throws IllegalAccessException, InstantiationException {
        Application application = RuntimeEnvironment.application;

        TestObserver<SupplicantStateChangedEvent> o = RxWifiManager.supplicantStateChanges(application).test();
        o.assertValues();

        Intent intent1 = new Intent(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION) //
                .putExtra(EXTRA_NEW_STATE, (Parcelable) SupplicantState.INACTIVE)
                .putExtra(EXTRA_SUPPLICANT_ERROR, ERROR_AUTHENTICATING);
        application.sendBroadcast(intent1);
        SupplicantStateChangedEvent event1 =
                SupplicantStateChangedEvent.create(SupplicantState.INACTIVE, ERROR_AUTHENTICATING);
        o.assertValues(event1);

        Intent intent2 = new Intent(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION) //
                .putExtra(EXTRA_NEW_STATE, (Parcelable) SupplicantState.ASSOCIATING)
                .putExtra(EXTRA_SUPPLICANT_ERROR, -1);
        application.sendBroadcast(intent2);
        SupplicantStateChangedEvent event2 =
                SupplicantStateChangedEvent.create(SupplicantState.ASSOCIATING, -1);
        o.assertValues(event1, event2);
    }

    @SuppressWarnings("ResourceType")
    @Test //
    public void supplicantConnectionChanges() throws IllegalAccessException, InstantiationException {
        Application application = RuntimeEnvironment.application;

        TestObserver<Boolean> o = RxWifiManager.supplicantConnectionChanges(application).test();
        o.assertValues();

        Intent intent1 = new Intent(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION) //
                .putExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, true);
        application.sendBroadcast(intent1);
        o.assertValues(true);

        Intent intent2 = new Intent(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION) //
                .putExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
        application.sendBroadcast(intent2);
        o.assertValues(true, false);
    }
}