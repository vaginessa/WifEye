package mahorad.com.wifeye.broadcast.rx.battery;

import android.app.Application;
import android.content.Intent;
import android.os.BatteryManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import io.reactivex.observers.TestObserver;

import static android.os.BatteryManager.EXTRA_HEALTH;
import static android.os.BatteryManager.EXTRA_ICON_SMALL;
import static android.os.BatteryManager.EXTRA_LEVEL;
import static android.os.BatteryManager.EXTRA_PLUGGED;
import static android.os.BatteryManager.EXTRA_PRESENT;
import static android.os.BatteryManager.EXTRA_SCALE;
import static android.os.BatteryManager.EXTRA_STATUS;
import static android.os.BatteryManager.EXTRA_TECHNOLOGY;
import static android.os.BatteryManager.EXTRA_TEMPERATURE;
import static android.os.BatteryManager.EXTRA_VOLTAGE;
import static mahorad.com.wifeye.broadcast.rx.battery.BatteryHealth.COLD;
import static mahorad.com.wifeye.broadcast.rx.battery.BatteryStatus.CHARGING;

@RunWith(RobolectricTestRunner.class)
public class RxBatteryManagerTest {
    @Test
    public void batteryStateChanges() {
        Application application = RuntimeEnvironment.application;

        TestObserver<BatteryState> o = RxBatteryManager.batteryStateChanges(application).test();
        o.assertValues();

        Intent intent1 = new Intent(Intent.ACTION_BATTERY_CHANGED)
                .putExtra(EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_COLD)
                .putExtra(EXTRA_ICON_SMALL, 0x3def2)
                .putExtra(EXTRA_LEVEL, 10)
                .putExtra(EXTRA_PLUGGED, 0)
                .putExtra(EXTRA_PRESENT, true)
                .putExtra(EXTRA_SCALE, 100)
                .putExtra(EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING)
                .putExtra(EXTRA_TECHNOLOGY, "unknown")
                .putExtra(EXTRA_TEMPERATURE, 40)
                .putExtra(EXTRA_VOLTAGE, 10000);
        application.sendBroadcast(intent1);
        BatteryState event1 =
                BatteryState.create(COLD, 0x3def2, 10, 0, true, 100, CHARGING, "unknown", 40, 10000);
        o.assertValues(event1);
    }
}