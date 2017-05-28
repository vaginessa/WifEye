package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import java.util.concurrent.TimeUnit;

public class UnaryCountdownBuilder {

    private static final int MINIMUM_DURATION = 1;
    private static final int MINIMUM_RUN_TIMES = 1;
    private static final Runnable NONE = () -> {};

    private int runTimes = MINIMUM_RUN_TIMES;
    private int duration = MINIMUM_DURATION;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private Runnable intervalsAction = NONE;
    private Runnable exceptionAction = NONE;
    private Runnable completedAction = NONE;

    public UnaryCountdownBuilder setRunTimes(int times) {
        runTimes = Math.max(times, MINIMUM_RUN_TIMES);
        return this;
    }

    public UnaryCountdownBuilder setDuration(int duration, TimeUnit unit) {
        this.duration = Math.max(duration, MINIMUM_DURATION);
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

    public int runTimes() {
        return runTimes;
    }

    public int duration() {
        return duration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

}