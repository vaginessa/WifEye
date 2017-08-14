package mahorad.com.wifeye.broadcast.manager.app.internet;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;

@AutoValue
public abstract class InternetStateChangedEvent {

    @NonNull
    public abstract String ssid();

    public abstract boolean connected();

    @CheckResult
    @NonNull
    public static InternetStateChangedEvent create(@NonNull String ssid, boolean connected) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        InternetStateChangedEvent e = new AutoValue_InternetStateChangedEvent(ssid, connected);
        return e;
    }
}