package wifeye.app.android.mahorad.com.wifeye.app.rx;

import android.app.Application;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import io.reactivex.observers.TestObserver;
import wifeye.app.android.mahorad.com.wifeye.app.events.WifiEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;

import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;
import static android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Disabled;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Enabled;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Unknown;

@RunWith(RobolectricTestRunner.class) //
public class WifiTest {

    @Test
    public void wifiEventChanges() {
        Application application = RuntimeEnvironment.application;

        TestObserver<WifiEvent> o = Wifi.observable(application).test();
        o.assertValues();

        Intent intent1 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_DISABLED);
        application.sendBroadcast(intent1);
        WifiEvent wifiEvent1 = WifiEvent.create(Disabled, Wifi.date());
        o.assertValues(wifiEvent1);

        Intent intent2 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_UNKNOWN);
        application.sendBroadcast(intent2);
        WifiEvent wifiEvent2 = WifiEvent.create(Unknown, Wifi.date());
        o.assertValues(wifiEvent1, wifiEvent2);

        Intent intent3 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_ENABLED);
        application.sendBroadcast(intent3);
        WifiEvent wifiEvent3 = WifiEvent.create(Enabled, Wifi.date());
        o.assertValues(wifiEvent1, wifiEvent2, wifiEvent3);

        Intent intent4 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_ENABLED);
        application.sendBroadcast(intent4);
        o.assertValues(wifiEvent1, wifiEvent2, wifiEvent3);

        Intent intent5 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_DISABLED);
        application.sendBroadcast(intent5);
        WifiEvent wifiEvent5 = WifiEvent.create(Disabled, Wifi.date());
        o.assertValues(wifiEvent1, wifiEvent2, wifiEvent3, wifiEvent5);

        o.cancel();

        Intent intent6 = new Intent(WIFI_STATE_CHANGED_ACTION).putExtra(EXTRA_WIFI_STATE, WIFI_STATE_DISABLED);
        application.sendBroadcast(intent6);
        o.assertValues(wifiEvent1, wifiEvent2, wifiEvent3, wifiEvent5);
    }
}