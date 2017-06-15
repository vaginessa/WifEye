package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState.Type;

@AutoValue
public abstract class EngineEvent {

    @NonNull
    public abstract Type type();

    @NonNull
    public abstract Date date();

    @CheckResult
    @NonNull
    public static EngineEvent create(@NonNull Type type, @NonNull Date date) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(date);
        return new AutoValue_EngineEvent(type, date);
    }
}
