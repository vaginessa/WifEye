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

    @CheckResult
    @NonNull
    public static PersistenceChangedEvent create(@NonNull String ssid, @NonNull String ctid) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        Preconditions.checkNotNull(ctid, "ctid == null");
        PersistenceChangedEvent e = new AutoValue_PersistenceChangedEvent(ssid, ctid);
        return e;
    }
}
