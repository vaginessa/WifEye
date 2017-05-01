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

    private Disposable timer;
    private Scheduler scheduler;

    private int enacts;
    private int length;
    private TimeUnit unit;
    private boolean isActive;

    public UnaryCountdown(UnaryCountdownBuilder builder) {
        if (builder == null)
            throw new IllegalArgumentException();
        enacts = builder.enacts();
        length = builder.length();
        unit = builder.timeUnit();

        intervalsAction = builder.intervalsAction();
        exceptionAction = builder.exceptionAction();
        completedAction = builder.completionAction();
    }

    public void start() {
        if (isActive) return;
        isActive = true;
        scheduler = Schedulers.newThread();
        timer = Observable
                .interval(length, unit, scheduler)
                .doOnNext(n -> intervalsAction.run())
                .takeWhile(n -> isActive && ++n < enacts)
                .doOnError(t -> { exceptionAction.run(); stop(); })
                .doOnComplete(() -> { completedAction.run(); stop(); })
                .subscribe();
    }

    public void stop() {
        if (!isActive) return;
        isActive = false;
        timer.dispose();
        scheduler.shutdown();
    }

    public boolean isActive() {
        return isActive;
    }

}
