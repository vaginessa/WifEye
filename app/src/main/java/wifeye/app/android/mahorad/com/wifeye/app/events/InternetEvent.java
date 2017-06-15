package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;

@AutoValue
public abstract class InternetEvent {

    @NonNull
    public abstract String ssid();

    public abstract boolean connected();

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static InternetEvent create(@NonNull String ssid, boolean connected, Date date) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        Preconditions.checkNotNull(date, "date == null");
        InternetEvent e = new AutoValue_InternetEvent(ssid, connected);
        e.date = date;
        return e;
    }
}
