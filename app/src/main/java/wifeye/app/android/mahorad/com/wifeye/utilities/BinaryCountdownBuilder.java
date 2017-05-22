package wifeye.app.android.mahorad.com.wifeye.utilities;

import java.util.concurrent.TimeUnit;

public class BinaryCountdownBuilder {

    private static final int MINIMUM_LESS_DELAYED_LENGTH = 1;
    private static final int MINIMUM_MORE_DELAYED_LENGTH = 2;
    private static final int MINIMUM_RUN_TIMES = 1;
    private static final Runnable NONE = () -> {};

    private int runTimes = MINIMUM_RUN_TIMES;
    private boolean startsWithMoreDelayedAction;

    private int lessDelayedLength = MINIMUM_LESS_DELAYED_LENGTH;
    private TimeUnit lessDelayedUnit;
    private Runnable lessDelayedAction = NONE;

    private int moreDelayedLength = MINIMUM_MORE_DELAYED_LENGTH;
    private TimeUnit moreDelayedUnit;
    private Runnable moreDelayedAction = NONE;

    private Runnable exceptionAction = NONE;
    private Runnable completionAction = NONE;

    public BinaryCountdownBuilder setRunTimes(int times) {
        runTimes = Math.max(times, MINIMUM_RUN_TIMES);
        return this;
    }

    public BinaryCountdownBuilder setLessDelayedLength(int Delayed, TimeUnit unit) {
        lessDelayedLength = Math.max(Delayed, MINIMUM_LESS_DELAYED_LENGTH);
        if (unit == null)
            return this;
        lessDelayedUnit = unit;
        return this;
    }

    public BinaryCountdownBuilder setMoreDelayedLength(int Delayed, TimeUnit unit) {
        moreDelayedLength = Math.max(Delayed, MINIMUM_MORE_DELAYED_LENGTH);
        if (unit == null)
            return this;
        moreDelayedUnit = unit;
        return this;
    }

    public BinaryCountdownBuilder setLessDelayedAction(Runnable r) {
        if (r == null)
            return this;
        lessDelayedAction = r;
        return this;
    }

    public BinaryCountdownBuilder setMoreDelayedAction(Runnable r) {
        if (r == null)
            return this;
        moreDelayedAction = r;
        return this;
    }

    public BinaryCountdownBuilder setExceptionAction(Runnable r) {
        if (r == null)
            return this;
        exceptionAction = r;
        return this;
    }

    public BinaryCountdownBuilder setCompletionAction(Runnable r) {
        if (r == null)
            return this;
        completionAction = r;
        return this;
    }

    public BinaryCountdownBuilder startWithMoreDelayedAction() {
        startsWithMoreDelayedAction = true;
        return this;
    }

    public BinaryCountdownBuilder startWithLessDelayedAction() {
        startsWithMoreDelayedAction = false;
        return this;
    }

    public int runTimes() {
        return runTimes;
    }

    public int lessDelayedLength() {
        return lessDelayedLength;
    }

    public TimeUnit lessDelayedUnit() {
        return lessDelayedUnit;
    }

    public Runnable lessDelayedAction() {
        return lessDelayedAction;
    }

    public int moreDelayedLength() {
        return moreDelayedLength;
    }

    public TimeUnit moreDelayedUnit() {
        return moreDelayedUnit;
    }

    public Runnable moreDelayedAction() {
        return moreDelayedAction;
    }

    public Runnable exceptionAction() {
        return exceptionAction;
    }

    public Runnable completionAction() {
        return completionAction;
    }

    public BinaryCountdown build() {
        return new BinaryCountdown(this);
    }

    public boolean isStartingWithMoreDelayedAction() {
        return startsWithMoreDelayedAction;
    }
}
