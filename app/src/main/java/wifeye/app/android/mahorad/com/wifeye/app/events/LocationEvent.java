package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;

@AutoValue
public abstract class LocationEvent {

    @NonNull
    public abstract String ctid();

    public abstract boolean known();

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static LocationEvent create(@NonNull String ctid, boolean known, @NonNull Date date) {
        Preconditions.checkNotNull(ctid, "ctid == null");
        Preconditions.checkNotNull(date, "date == null");
        LocationEvent e = new AutoValue_LocationEvent(ctid, known);
        e.date = date;
        return e;
    }

}
