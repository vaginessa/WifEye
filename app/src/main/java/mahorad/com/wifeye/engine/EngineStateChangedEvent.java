package mahorad.com.wifeye.engine;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;
import mahorad.com.wifeye.engine.state.IState.Type;

/**
 * Created by mahan on 2017-08-13.
 */
@AutoValue
public abstract class EngineStateChangedEvent {

    @NonNull
    public abstract Type type();

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static EngineStateChangedEvent create(@NonNull Type type, @NonNull Date date) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(date);
        EngineStateChangedEvent e = new AutoValue_EngineStateChangedEvent(type);
        e.date = date;
        return e;
    }
}