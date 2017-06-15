package wifeye.app.android.mahorad.com.wifeye.app.events;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

import dagger.internal.Preconditions;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type;

@AutoValue
public abstract class ActionEvent {

    @NonNull
    public abstract Type type();

    private Date date;
    public final Date date() {
        return date;
    }

    @CheckResult
    @NonNull
    public static ActionEvent create(@NonNull Type type, @NonNull Date date) {
        Preconditions.checkNotNull(type, "type == null");
        Preconditions.checkNotNull(date, "date == null");
        ActionEvent e = new AutoValue_ActionEvent(type);
        e.date = date;
        return e;
    }
}
