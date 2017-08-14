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

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static InternetStateChangedEvent create(@NonNull String ssid, boolean connected, Date date) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        Preconditions.checkNotNull(date, "date == null");
        InternetStateChangedEvent e = new AutoValue_InternetStateChangedEvent(ssid, connected);
        e.date = date;
        return e;
    }
}