package mahorad.com.wifeye.broadcast.manager.app.persist;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;

@AutoValue
public abstract class PersistenceChangedEvent {

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
    public static PersistenceChangedEvent create(@NonNull String ssid, @NonNull String ctid, @NonNull Date date) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        Preconditions.checkNotNull(ctid, "ctid == null");
        Preconditions.checkNotNull(date, "date == null");
        PersistenceChangedEvent e = new AutoValue_PersistenceChangesEvent(ssid, ctid);
        e.date = date;
        return e;
    }
}
