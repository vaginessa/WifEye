package mahorad.com.wifeye.util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * An RxJava based countdown timer which performs
 * the given runnable after countdown finishes.
 */
public class UnaryCountdown {

    private Runnable completedAction;
    private BooleanSupplier condition;

    private Disposable timer;
    private Scheduler scheduler;

    private int duration;
    private TimeUnit unit;
    private boolean isActive;

    private PublishSubject<Long> elapsed = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private HashMap<Consumer<Long>, Disposable> consumers = new HashMap<>();

    public UnaryCountdown(UnaryCountdownBuilder builder) {
        if (builder == null)
            throw new IllegalArgumentException();
        duration = builder.duration();
        unit = builder.timeUnit();

        completedAction = builder.completionAction();
        condition = builder.condition();
    }

    public synchronized void start() {
        if (isActive) return;
        isActive = true;
        scheduler = Schedulers.newThread();
        updateSubscriptions();
        timer = Observable
                .interval(1, unit, scheduler)
                .doOnNext(n -> elapsed.onNext(n))
                .takeWhile(this::canContinue)
                .doOnComplete(() -> {
                    if (!isActive) return;
                    completedAction.run();
                    stop();
                })
                .subscribe();
    }

    private boolean canContinue(Long n) throws Exception {
        boolean canTake = isActive && ++n < duration;
        if (!canTake) return false;
        return condition == null || condition.getAsBoolean();
    }

    private void updateSubscriptions() {
        Observable
                .fromIterable(consumers.keySet())
                .subscribe(this::subscribe);
    }

    public void subscribe(Consumer<Long> consumer) {
        if (consumer == null) return;
        if (hasUnused(consumer)) return;
        Disposable disposable = elapsed.subscribe(consumer);
        consumers.put(consumer, disposable);
        compositeDisposable.add(disposable);
    }

    private boolean hasUnused(Consumer<Long> consumer) {
        boolean exists = consumers.containsKey(consumer);
        return exists && !consumers.get(consumer).isDisposed();
    }

    public void unsubscribe(Consumer<Long> consumer) {
        if (consumer == null) return;
        if (!consumers.containsKey(consumer)) return;
        Disposable disposable = consumers.get(consumer);
        consumers.remove(consumer);
        compositeDisposable.remove(disposable);
    }

    public synchronized void stop() {
        if (!isActive) return;
        scheduler.shutdown();
        timer.dispose();
        compositeDisposable.clear();
        timer = null;
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
