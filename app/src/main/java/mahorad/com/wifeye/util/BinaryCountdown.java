package mahorad.com.wifeye.util;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;

public class BinaryCountdown {

    private static final int MORE_DELAY = 1;
    private static final int LESS_DELAY = -1;

    private int ranTimes;
    private int runTimes;
    private int runningCountdownTimer;

    private boolean startsWithMoreDelayedAction;

    private final UnaryCountdown lessDelayedCountdown;
    private int lessDelayedLength;
    private TimeUnit lessDelayedUnit;
    private Runnable lessDelayedAction;
    private BooleanSupplier lessDelayedCondition;

    private final UnaryCountdown moreDelayedCountdown;
    private int moreDelayedLength;
    private TimeUnit moreDelayedUnit;
    private Runnable moreDelayedAction;
    private BooleanSupplier moreDelayedCondition;

    private Runnable exceptionAction;
    private Runnable completionAction;

    public BinaryCountdown(BinaryCountdownBuilder builder) {
        runTimes = builder.runTimes();
        startsWithMoreDelayedAction = builder.isStartingWithMoreDelayedAction();

        lessDelayedLength = builder.lessDelayedLength();
        lessDelayedUnit = builder.lessDelayedUnit();
        lessDelayedAction = builder.lessDelayedAction();
        lessDelayedCondition = builder.lessDelayedCondition();
        lessDelayedCountdown = createLessDelayedTimer();

        moreDelayedLength = builder.moreDelayedLength();
        moreDelayedUnit = builder.moreDelayedUnit();
        moreDelayedAction = builder.moreDelayedAction();
        moreDelayedCondition = builder.moreDelayedCondition();
        moreDelayedCountdown = createMoreDelayedTimer();

        exceptionAction = builder.exceptionAction();
        completionAction = builder.completionAction();
    }

    private UnaryCountdown createMoreDelayedTimer() {
        return UnaryCountdown
                .builder()
                .setDuration(moreDelayedLength, moreDelayedUnit)
                .setCondition(moreDelayedCondition)
                .setCompletionAction(() -> {
                    try {
                        moreDelayedAction.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptionAction.run();
                    }
                    reset();
                })
                .build();
    }

    private UnaryCountdown createLessDelayedTimer() {
        return UnaryCountdown
                .builder()
                .setDuration(lessDelayedLength, lessDelayedUnit)
                .setCondition(lessDelayedCondition)
                .setCompletionAction(() -> {
                    try {
                        lessDelayedAction.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptionAction.run();
                    }
                    reset();
                })
                .build();
    }

    public void start() {
        if (isActive()) return;
        if (startsWithMoreDelayedAction) {
            runningCountdownTimer = 1;
            moreDelayedCountdown.start();
        } else {
            runningCountdownTimer = -1;
            lessDelayedCountdown.start();
        }
    }

    private void reset() {
        if (++ranTimes >= 2 * runTimes) {
            completionAction.run();
            stop();
        } else {
            Executors
                    .newSingleThreadExecutor()
                    .submit(this::startNextTimer);
        }
    }

    private void startNextTimer() {
        if (runningCountdownTimer == MORE_DELAY) {
            runningCountdownTimer = LESS_DELAY;
            lessDelayedCountdown.start();
        }
        else if (runningCountdownTimer == LESS_DELAY) {
            runningCountdownTimer = MORE_DELAY;
            moreDelayedCountdown.start();
        }
    }

    public void stop() {
        moreDelayedCountdown.stop();
        lessDelayedCountdown.stop();
        ranTimes = 0;
        runningCountdownTimer = 0;
    }

    public boolean isActive() {
        if (isLessDelayedCountdownActive())
            return true;
        if (isMoreDelayedCountdownActive())
            return true;
        return false;
    }

    private boolean isMoreDelayedCountdownActive() {
        if (moreDelayedCountdown == null)
            return false;
        return moreDelayedCountdown.isActive();
    }

    private boolean isLessDelayedCountdownActive() {
        if (lessDelayedCountdown == null)
            return false;
        return lessDelayedCountdown.isActive();
    }

    public void subscribe(Consumer<Long> consumer) {
        moreDelayedCountdown.subscribe(consumer);
        lessDelayedCountdown.subscribe(consumer);
    }

    public void ubsubscribe(Consumer<Long> consumer) {
        moreDelayedCountdown.unsubscribe(consumer);
        lessDelayedCountdown.unsubscribe(consumer);
    }

    public static BinaryCountdownBuilder builder() {
        return new BinaryCountdownBuilder();
    }
}
