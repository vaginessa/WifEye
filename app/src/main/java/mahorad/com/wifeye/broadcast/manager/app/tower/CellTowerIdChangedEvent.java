package mahorad.com.wifeye.broadcast.manager.app.tower;

/**
 * Created by mahan on 2017-08-13.
 */
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;

@AutoValue
public abstract class CellTowerIdChangedEvent {

    @NonNull
    public abstract String ctid();

    public abstract boolean known();

    @CheckResult
    @NonNull
    public static CellTowerIdChangedEvent create(@NonNull String ctid, boolean known) {
        Preconditions.checkNotNull(ctid, "ctid == null");
        CellTowerIdChangedEvent e = new AutoValue_CellTowerIdChangedEvent(ctid, known);
        return e;
    }

}

