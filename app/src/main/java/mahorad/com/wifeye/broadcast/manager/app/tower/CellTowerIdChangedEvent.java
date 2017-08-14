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

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static CellTowerIdChangedEvent create(@NonNull String ctid, boolean known, @NonNull Date date) {
        Preconditions.checkNotNull(ctid, "ctid == null");
        Preconditions.checkNotNull(date, "date == null");
        CellTowerIdChangedEvent e = new AutoValue_CellTowerIdChangedEvent(ctid, known);
        e.date = date;
        return e;
    }

}

