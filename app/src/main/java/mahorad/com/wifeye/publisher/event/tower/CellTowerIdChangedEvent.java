package mahorad.com.wifeye.publisher.event.tower;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import dagger.internal.Preconditions;

/**
 * Created by mahan on 2017-08-13.
 */

@AutoValue
public abstract class CellTowerIdChangedEvent {

    @NonNull
    public abstract String ctid();

    public abstract boolean known();

    @CheckResult
    @NonNull
    public static CellTowerIdChangedEvent create(@NonNull String ctid, boolean known) {
        Preconditions.checkNotNull(ctid, "ctid == null");
        return new AutoValue_CellTowerIdChangedEvent(ctid, known);
    }

}

