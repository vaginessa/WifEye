package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.BooleanSupplier;

public class UnaryCountdownBuilder {

    private static final int MINIMUM_DURATION = 1;
    private static final Runnable NONE = () -> {};

    private int duration = MINIMUM_DURATION;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private Runnable exceptionAction = NONE;
    private Runnable completedAction = NONE;

    private BooleanSupplier condition;

    public UnaryCountdownBuilder setDuration(int duration, TimeUnit unit) {
        this.duration = Math.max(duration, MINIMUM_DURATION);
        if (unit == null)
            return this;
        timeUnit = unit;
        return this;
    }

    public UnaryCountdownBuilder setExceptionAction(Runnable r) {
        if (r == null)
            return this;
        exceptionAction = r;
        return this;
    }

    public UnaryCountdownBuilder setCompletionAction(Runnable r) {
        if (r == null)
            return this;
        completedAction = r;
        return this;
    }

    public UnaryCountdownBuilder setCondition(BooleanSupplier condition) {
        if (condition == null)
            return this;
        this.condition = condition;
        return this;
    }

    public UnaryCountdown build() {
        return new UnaryCountdown(this);
    }

    public Runnable exceptionAction() {
        return exceptionAction;
    }

    public Runnable completionAction() {
        return completedAction;
    }

    public int duration() {
        return duration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

    public BooleanSupplier condition() {
        return condition;
    }
}
