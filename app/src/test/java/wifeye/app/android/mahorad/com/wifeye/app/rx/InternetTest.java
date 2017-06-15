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
import wifeye.app.android.mahorad.com.wifeye.app.events.InternetEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
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
public class InternetTest {

    @SuppressWarnings("ResourceType")
    @Test
    public void networkEventChanges() throws IllegalAccessException, InstantiationException {
        Application application = RuntimeEnvironment.application;

        TestObserver<InternetEvent> o = Internet.observable(application).test();
        o.assertValues();

        NetworkInfo networkInfo1 = NetworkInfo.class.newInstance();
        WifiInfo wifiInfo1 = WifiInfo.class.newInstance();
        Intent intent1 = new Intent(NETWORK_STATE_CHANGED_ACTION) //
                .putExtra(WifiManager.EXTRA_NETWORK_INFO, networkInfo1)
                .putExtra(WifiManager.EXTRA_BSSID, "foo")
                .putExtra(WifiManager.EXTRA_WIFI_INFO, wifiInfo1);
        application.sendBroadcast(intent1);
        InternetEvent event1 = InternetEvent.create("foo", true, Internet.date());
        o.assertValues(event1);

//        NetworkInfo networkInfo2 = NetworkInfo.class.newInstance();
//        Intent intent2 = new Intent(NETWORK_STATE_CHANGED_ACTION) //
//                .putExtra(WifiManager.EXTRA_NETWORK_INFO, networkInfo2);
//        application.sendBroadcast(intent2);
//        NetworkStateChangedEvent event2 = NetworkStateChangedEvent.create(networkInfo2, null, null);
//        o.assertValues(event1, event2);
    }

}