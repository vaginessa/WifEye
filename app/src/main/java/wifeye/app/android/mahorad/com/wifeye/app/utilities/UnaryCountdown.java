package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * An RxJava based countdown timer which performs
 * the given runnable after countdown finishes.
 */
public class UnaryCountdown {

    private Runnable completedAction;

    private Observable<Long> observable;
    private Disposable disposable;
    private Scheduler scheduler;

    private int duration;
    private TimeUnit unit;
    private boolean isActive;

    private volatile AtomicLong elapsed = new AtomicLong(0);

    public UnaryCountdown(UnaryCountdownBuilder builder) {
        if (builder == null)
            throw new IllegalArgumentException();
        duration = builder.duration();
        unit = builder.timeUnit();

        completedAction = builder.completionAction();
    }

    public synchronized void start() {
        if (isActive) return;
        isActive = true;
        scheduler = Schedulers.newThread();
        disposable = Observable
                .interval(1, unit, scheduler)
                .doOnNext(n -> elapsed.set(n + 1))
                .takeWhile(n -> isActive && ++n < duration)
                .doOnComplete(() -> {
                    completedAction.run();
                    stop();
                })
                .subscribe();
    }

    public synchronized long elapsed() {
        return elapsed.get();
    }

    public synchronized void stop() {
        if (!isActive) return;
        scheduler.shutdown();
        disposable.dispose();
        observable = null;
        disposable = null;
        scheduler = null;
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public int durationTime() {
        return duration;
    }

    public TimeUnit durationUnit() {
        return unit;
    }

    public static UnaryCountdownBuilder builder() {
        return new UnaryCountdownBuilder();
    }
}
