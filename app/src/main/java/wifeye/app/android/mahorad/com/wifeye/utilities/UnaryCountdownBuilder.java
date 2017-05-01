package wifeye.app.android.mahorad.com.wifeye.utilities;

import java.util.concurrent.TimeUnit;

public class UnaryCountdownBuilder {

    private static final int MINIMUM_PERIOD = 1;
    private static final int MINIMUM_ENACTS = 1;
    private static final Runnable NONE = () -> {};

    private int enacts = MINIMUM_ENACTS;
    private int length = MINIMUM_PERIOD;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private Runnable intervalsAction = NONE;
    private Runnable exceptionAction = NONE;
    private Runnable completedAction = NONE;

    public UnaryCountdownBuilder setEnacts(int times) {
        enacts = Math.max(times, MINIMUM_ENACTS);
        return this;
    }

    public UnaryCountdownBuilder setLength(int length, TimeUnit unit) {
        this.length = Math.max(length, MINIMUM_PERIOD);
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

    public int enacts() {
        return enacts;
    }

    public int length() {
        return length;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

}
