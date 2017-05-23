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
        createUnaryTimer();
        unaryTimer.start();
    }

    private void createUnaryTimer() {
        unaryTimer = startsWithMoreDelayedAction
                ? createMoreDelayedTimer()
                : createLessDelayedTimer();
    }

    private UnaryCountdown createMoreDelayedTimer() {
        return new UnaryCountdownBuilder()
                .setRunTimes(1)
                .setDuration(moreDelayedLength, moreDelayedUnit)
                .setIntervalsAction(moreDelayedAction)
                .setExceptionAction(exceptionAction)
                .setCompletionAction(this::startLessDelayedAction)
                .build();
    }

    private UnaryCountdown createLessDelayedTimer() {
        return new UnaryCountdownBuilder()
                .setRunTimes(1)
                .setDuration(lessDelayedLength, lessDelayedUnit)
                .setIntervalsAction(lessDelayedAction)
                .setExceptionAction(exceptionAction)
                .setCompletionAction(this::startMoreDelayedAction)
                .build();
    }

    private void startMoreDelayedAction() {
        new UnaryCountdownBuilder()
                .setRunTimes(1)
                .setDuration(moreDelayedLength, moreDelayedUnit)
                .setIntervalsAction(moreDelayedAction)
                .setExceptionAction(exceptionAction)
                .setCompletionAction(this::cycleIntervalAction)
                .build()
                .start();
    }

    private void startLessDelayedAction() {
        new UnaryCountdownBuilder()
                .setRunTimes(1)
                .setDuration(lessDelayedLength, lessDelayedUnit)
                .setIntervalsAction(lessDelayedAction)
                .setExceptionAction(exceptionAction)
                .setCompletionAction(this::cycleIntervalAction)
                .build()
                .start();
    }

    private void cycleIntervalAction() {
        if (++ranTimes >= runTimes) {
            runCompletionAction();
            stop();
        } else {
            start();
        }
    }

    private void runCompletionAction() {
        Executors
                .newSingleThreadExecutor()
                .submit(completionAction);
    }

    public void stop() {
        unaryTimer.stop();
        ranTimes = 0;
    }

    public boolean isActive() {
        if (unaryTimer == null)
            return false;
        return unaryTimer.isActive();
    }

    public long elapsed() {
        return unaryTimer.elapsed();
    }

}
