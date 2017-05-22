package wifeye.app.android.mahorad.com.wifeye.utilities;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * An RxJava based countdown timer which performs
 * the given runnable after countdown finishes.
 */
public class UnaryCountdown {

    private Runnable intervalsAction;
    private Runnable completedAction;
    private Runnable exceptionAction;

    private Observable<Long> observable;
    private Disposable disposable;
    private Scheduler scheduler;

    private int runTimes;
    private int duration;
    private TimeUnit unit;
    private boolean isActive;

    private long elapsed;

    public UnaryCountdown(UnaryCountdownBuilder builder) {
        if (builder == null)
            throw new IllegalArgumentException();
        runTimes = builder.runTimes();
        duration = builder.duration();
        unit = builder.timeUnit();

        intervalsAction = builder.intervalsAction();
        exceptionAction = builder.exceptionAction();
        completedAction = builder.completionAction();
    }

    public void start() {
        if (isActive) return;
        isActive = true;
        scheduler = Schedulers.newThread();
        observable = Observable
                .interval(duration, unit, scheduler)
                .doOnNext(n -> intervalsAction.run())
                .takeWhile(n -> isActive && ++n < runTimes)
                .doOnError(t -> {
                    exceptionAction.run();
                    stop();
                })
                .doOnComplete(() -> {
                    completedAction.run();
                    stop();
                });
        disposable = observable.subscribe(
                aLong -> elapsed = aLong
        );
    }

    public long elapsed() {
        return elapsed;
    }

    public void stop() {
        if (!isActive) return;
        isActive = false;
        elapsed = 0;
        disposable.dispose();
        scheduler.shutdown();
        observable = null;
        disposable = null;
        scheduler = null;
    }

    public boolean isActive() {
        return isActive;
    }

}
