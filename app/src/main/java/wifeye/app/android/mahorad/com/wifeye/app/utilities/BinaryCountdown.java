package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BinaryCountdown {

    private UnaryCountdown unaryTimer;

    private int ranTimes;
    private int runTimes;

    private boolean startsWithMoreDelayedAction;
    private int lessDelayedLength;
    private TimeUnit lessDelayedUnit;

    private Runnable lessDelayedAction;
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

        moreDelayedLength = builder.moreDelayedLength();
        moreDelayedUnit = builder.moreDelayedUnit();
        moreDelayedAction = builder.moreDelayedAction();

        exceptionAction = builder.exceptionAction();
        completionAction = builder.completionAction();
    }

    public void start() {
        if (isActive())
            return;
        reset();
    }

    private UnaryCountdown createMoreDelayedTimer() {
        return UnaryCountdown
                .builder()
                .setRunTimes(1)
                .setDuration(moreDelayedLength, moreDelayedUnit)
                .setIntervalsAction(moreDelayedAction)
                .setExceptionAction(exceptionAction)
                .setCompletionAction(this::reset)
                .build();
    }

    private UnaryCountdown createLessDelayedTimer() {
        return UnaryCountdown
                .builder()
                .setRunTimes(1)
                .setDuration(lessDelayedLength, lessDelayedUnit)
                .setIntervalsAction(lessDelayedAction)
                .setExceptionAction(exceptionAction)
                .setCompletionAction(this::reset)
                .build();
    }

    private void reset() {
        Executors.newSingleThreadExecutor()
                .submit(this::verifyCompletion);
    }

    private void verifyCompletion() {
        if (ranTimes++ >= 2 * runTimes) {
            runCompletionAction();
            stop();
        } else {
            createUnaryTimer();
            unaryTimer.start();
        }
    }

    private void createUnaryTimer() {
        if (unaryTimer != null) {
            unaryTimer = isLessDelayedAction()
                    ? createMoreDelayedTimer()
                    : createLessDelayedTimer();
        } else {
            unaryTimer = startsWithMoreDelayedAction
                    ? createMoreDelayedTimer()
                    : createLessDelayedTimer();
        }
    }

    private boolean isLessDelayedAction() {
        return
                unaryTimer.durationTime() == lessDelayedLength &&
                lessDelayedUnit.equals(unaryTimer.durationUnit());
    }

    private void runCompletionAction() {
        Executors
                .newSingleThreadExecutor()
                .submit(completionAction);
    }

    public void stop() {
        unaryTimer.stop();
        unaryTimer = null;
    }

    public boolean isActive() {
        if (unaryTimer == null)
            return false;
        return unaryTimer.isActive();
    }

    public long elapsed() {
        if (unaryTimer == null)
            return 0;
        return unaryTimer.elapsed();
    }

    public static BinaryCountdownBuilder builder() {
        return new BinaryCountdownBuilder();
    }
}
