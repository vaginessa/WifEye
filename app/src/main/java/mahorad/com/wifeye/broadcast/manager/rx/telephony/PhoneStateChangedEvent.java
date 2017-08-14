package mahorad.com.wifeye.broadcast.manager.rx.telephony;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import dagger.internal.Preconditions;

@AutoValue
public abstract class PhoneStateChangedEvent {

    @CheckResult
    @NonNull
    public static PhoneStateChangedEvent create(@NonNull String state, @Nullable String phoneNumber) {
        Preconditions.checkNotNull(state, "state == null");
        return new AutoValue_PhoneStateChangedEvent(state, phoneNumber);
    }

    public abstract @NonNull String state();

    public abstract @Nullable String incomingNumber();
}