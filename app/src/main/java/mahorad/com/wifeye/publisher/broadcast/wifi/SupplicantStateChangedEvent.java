package mahorad.com.wifeye.publisher.broadcast.wifi;

import android.net.wifi.SupplicantState;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import dagger.internal.Preconditions;

@AutoValue
public abstract class SupplicantStateChangedEvent {

    @CheckResult
    @NonNull
    public static SupplicantStateChangedEvent create(
            @NonNull SupplicantState newState, int error)
    {
        Preconditions.checkNotNull(newState, "newState == null");
        return new AutoValue_SupplicantStateChangedEvent(newState, error);
    }

    public abstract @NonNull SupplicantState newState();

    public abstract int error();
}