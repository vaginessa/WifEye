package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State;

@AutoValue
public abstract class WifiEvent {

    @NonNull
    public abstract State state();

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static WifiEvent create(@NonNull State state, @NonNull Date date) {
        Preconditions.checkNotNull(state);
        Preconditions.checkNotNull(date);
        WifiEvent e = new AutoValue_WifiEvent(state);
        e.date = date;
        return e;
    }
}
