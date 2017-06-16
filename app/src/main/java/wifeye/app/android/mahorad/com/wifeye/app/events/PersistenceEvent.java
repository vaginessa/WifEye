package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persistence;

@AutoValue
public abstract class PersistenceEvent {

    @NonNull
    public abstract String ssid();

    @NonNull
    public abstract String ctid();

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static PersistenceEvent create(@NonNull String ssid, @NonNull String ctid, @NonNull Date date) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        Preconditions.checkNotNull(ctid, "ctid == null");
        Preconditions.checkNotNull(date, "date == null");
        PersistenceEvent e = new AutoValue_PersistenceEvent(ssid, ctid);
        e.date = date;
        return e;
    }
}
