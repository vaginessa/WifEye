package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    private final UnaryCountdown moreDelayedCountdown;
    private int moreDelayedLength;
    private TimeUnit moreDelayedUnit;
    private Runnable moreDelayedAction;

    private Runnable exceptionAction;
    private Runnable completionAction;

    public BinaryCountdown(BinaryCountdownBuilder builder) {
        runTimes = builder.runTimes();
        startsWithMoreDelayedAction = builder.isStartingWithMoreDelayedAction();

        lessDelayedLength = builder.lessDelayedLength();
        lessDelayedUnit = builder.lessDelayedUnit();
        lessDelayedAction = builder.lessDelayedAction();
        lessDelayedCountdown = createLessDelayedTimer();

        moreDelayedLength = builder.moreDelayedLength();
        moreDelayedUnit = builder.moreDelayedUnit();
        moreDelayedAction = builder.moreDelayedAction();
        moreDelayedCountdown = createMoreDelayedTimer();

        exceptionAction = builder.exceptionAction();
        completionAction = builder.completionAction();
    }

    private UnaryCountdown createMoreDelayedTimer() {
        return UnaryCountdown
                .builder()
                .setDuration(moreDelayedLength, moreDelayedUnit)
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

    public long elapsed() {
        switch (runningCountdownTimer) {
            case  1: return moreDelayedCountdown.elapsed();
            case -1: return lessDelayedCountdown.elapsed();
            default: return 0;
        }
    }

    public static BinaryCountdownBuilder builder() {
        return new BinaryCountdownBuilder();
    }
}
