package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;

@AutoValue
public abstract class PersistenceEvent {

    @NonNull
    public abstract String ssid();

    @NonNull
    public abstract String ctid();

    @NonNull
    public abstract Date date();

    @CheckResult
    @NonNull
    public static PersistenceEvent create(@NonNull String ssid, @NonNull String ctid, @NonNull Date date) {
        Preconditions.checkNotNull(ssid, "ssid == null");
        Preconditions.checkNotNull(ctid, "ctid == null");
        Preconditions.checkNotNull(date, "date == null");
        return new AutoValue_PersistenceEvent(ssid, ctid, date);
    }
}
