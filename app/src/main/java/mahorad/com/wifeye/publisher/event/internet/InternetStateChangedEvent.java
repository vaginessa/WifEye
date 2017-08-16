package mahorad.com.wifeye.publisher.event.internet;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import dagger.internal.Preconditions;

/**
 * Created by mahan on 2017-08-13.
 */

@AutoValue
public abstract class InternetStateChangedEvent {

    @NonNull
    public abstract String ssid();

    public abstract boolean connected();

    @CheckResult
    @NonNull
    public static InternetStateChangedEvent create(@NonNull String ssid, boolean connected) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        return new AutoValue_InternetStateChangedEvent(ssid, connected);
    }
}