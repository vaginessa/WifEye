package wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import dagger.internal.Preconditions;

@AutoValue
public abstract class NetworkStateChangedEvent {

    @CheckResult
    @NonNull
    public static NetworkStateChangedEvent create(
            @NonNull NetworkInfo networkInfo,
            @Nullable String bssid,
            @Nullable WifiInfo wifiInfo)
    {
        Preconditions.checkNotNull(networkInfo, "networkInfo == null");
        return new AutoValue_NetworkStateChangedEvent(networkInfo, bssid, wifiInfo);
    }

    public abstract @NonNull NetworkInfo networkInfo();

    public abstract @Nullable String bssid();

    public abstract @Nullable WifiInfo wifiInfo();
}