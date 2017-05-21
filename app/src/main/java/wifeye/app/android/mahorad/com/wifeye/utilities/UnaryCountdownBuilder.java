package wifeye.app.android.mahorad.com.wifeye.utilities;

import java.util.concurrent.TimeUnit;

public class UnaryCountdownBuilder {

    private static final int MINIMUM_PERIOD = 1;
    private static final int MINIMUM_RESET_COUNT = 1;
    private static final Runnable NONE = () -> {};

    private int resetCount = MINIMUM_RESET_COUNT;
    private int duration = MINIMUM_PERIOD;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private Runnable intervalsAction = NONE;
    private Runnable exceptionAction = NONE;
    private Runnable completedAction = NONE;

    public UnaryCountdownBuilder setResetCount(int times) {
        resetCount = Math.max(times, MINIMUM_RESET_COUNT);
        return this;
    }

    public UnaryCountdownBuilder setDuration(int length, TimeUnit unit) {
        this.duration = Math.max(length, MINIMUM_PERIOD);
        if (unit == null)
            return this;
        timeUnit = unit;
        return this;
    }

    public UnaryCountdownBuilder setIntervalsAction(Runnable r) {
        if (r == null)
            return this;
        intervalsAction = r;
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

    public UnaryCountdown build() {
        return new UnaryCountdown(this);
    }

    public Runnable intervalsAction() {
        return intervalsAction;
    }

    public Runnable exceptionAction() {
        return exceptionAction;
    }

    public Runnable completionAction() {
        return completedAction;
    }

    public int resetCount() {
        return resetCount;
    }

    public int duration() {
        return duration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

}
