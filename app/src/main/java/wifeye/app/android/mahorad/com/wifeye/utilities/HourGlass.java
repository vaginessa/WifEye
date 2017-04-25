package wifeye.app.android.mahorad.com.wifeye.utilities;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * An RxJava based countdown timer which performs
 * the given runnable after countdown finishes.
 */
public class HourGlass {

    private static final String TAG = HourGlass.class.getSimpleName();

    private static final int DURATION_IN_SECONDS = 3;
    private static final int MINIMUM_FLIP_COUNTS = 1;
    private static final Runnable NO_ACTION = new Runnable() {
        @Override
        public void run() {}
    };

    private final Runnable onNextFlip;
    private final Runnable onComplete;
    private final Runnable onException;

    private Disposable timer;
    private Scheduler scheduler;
    private String name = "";

    private int flips = MINIMUM_FLIP_COUNTS;
    private int duration = DURATION_IN_SECONDS;
    private boolean isActive;

    private final Predicate<Long> lessThanFlipsCount = new Predicate<Long>() {
        @Override
        public boolean test(@NonNull Long aLong) throws Exception {
            return isActive && aLong < flips - 1;
        }
    };

    private final Consumer<Long> nextFlipAction = new Consumer<Long>() {
        @Override
        public void accept(@NonNull Long aLong) throws Exception {
            Log.d(TAG, String.format("ITERATION %d DURATION %d", aLong + 1, duration));
            onNextFlip.run();
        }
    };

    private final Consumer<Throwable> exceptionAction = new Consumer<Throwable>() {
        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
            Log.e(TAG, String.format("EXCEPTION IN %s", getName()), throwable);
            onException.run();
        }
    };

    private final Action completedAction = new Action() {
        @Override
        public void run() throws Exception {
            Log.d(TAG, String.format("COMPLETED %s", getName()));
            stop();
            onComplete.run();
        }
    };

    /**
     * creates an hourglass that repeats triggering an
     * action for given count and on certain intervals
     * @param onNextFlip the runnable that occurs after
     *                   the flipped hourglass finishes
     */
    public HourGlass(Runnable onNextFlip) {
        this(onNextFlip, NO_ACTION, NO_ACTION);
    }

    /**
     * creates an hourglass that repeats triggering an
     * action for given count and on certain intervals
     * @param onNextFlip the runnable that occurs after
     *                   the flipped hourglass finishes
     * @param onComplete the runnable that occurs after
     *                   all hourglass flips are done
     */
    public HourGlass(Runnable onNextFlip, Runnable onComplete) {
        this(onNextFlip, NO_ACTION, onComplete);
    }

    /**
     * creates an hourglass that repeats triggering an
     * action for given count and on certain intervals
     * @param onNextFlip the runnable that occurs after
     *                   the flipped hourglass finishes
     * @param onException the runnable that occurs when an
     *                    exception occurs during execution
     * @param onComplete the runnable that occurs after
     *                   all hourglass flips are done
     */
    public HourGlass(Runnable onNextFlip, Runnable onException, Runnable onComplete) {
        if (onNextFlip == null)
            throw new IllegalArgumentException();
        if (onException == null)
            throw new IllegalArgumentException();
        if (onComplete == null)
            throw new IllegalArgumentException();
        this.onNextFlip = onNextFlip;
        this.onException = onException;
        this.onComplete = onComplete;
    }

    public void start() {
        if (isActive) return;
        isActive = true;
        scheduler = Schedulers.newThread();
        timer = Observable
                .interval(duration, SECONDS, scheduler)
                .takeWhile(lessThanFlipsCount)
                .doOnNext(nextFlipAction)
                .doOnError(exceptionAction)
                .doOnComplete(completedAction)
                .subscribe();
        Log.d(TAG, String.format("STARTED %s", getName()));
    }

    public void stop() {
        if (!isActive) return;
        isActive = false;
        timer.dispose();
        scheduler.shutdown();
        Log.d(TAG, String.format("STOPPED %s", getName()));
    }

    public void setFlips(int flipsCount) {
        if (flipsCount < 1)
            return;
        flips = flipsCount;
    }

    public void setDuration(int seconds) {
        if (duration < 1)
            return;
        duration = seconds;
    }

    public boolean isActive() {
        return isActive;
    }

    @android.support.annotation.NonNull
    private String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            return;
        this.name = name;
    }
}