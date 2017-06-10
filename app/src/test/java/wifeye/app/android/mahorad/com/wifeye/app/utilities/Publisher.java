package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import org.junit.Test;
import org.reactivestreams.Subscriber;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;
import io.reactivex.schedulers.Schedulers;

import static com.jayway.awaitility.Awaitility.await;
import static dagger.internal.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Publisher {

    BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

    private int count = 1;
    private ObservableOnSubscribe<Integer> source = oe -> {
        Integer last = -1;
        while (count < 50) {
            try {
                Integer thiz = queue.take();
                if (thiz == last) continue;
                last = thiz;
                oe.onNext(thiz);
            } catch (InterruptedException e) {
                oe.onError(e);
            }
        }
        oe.onComplete();
    };
    void produce() {
        Random random = new Random();
        while (count++ < 50) {
            int element = random.nextInt(10);
            System.out.println("produced: " + element);
            try {
                queue.put(element);
            } catch (InterruptedException e) {

            }
        }
    }

    private Observable<Integer> events() {
        return Observable.create(source);
    }


    @Test
    public void test() {

        events()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        (e) -> { System.out.println("consuming: " + e); },
                        (t) -> { System.out.println(t); },
                        ( ) -> { System.out.println("completed");}
                );

        produce();
        await().atMost(55, SECONDS).until(() -> count == 50);


    }

    @CheckResult
    @NonNull //
    public static Observable<Intent> create(@NonNull final Context context,
                                            @NonNull final IntentFilter intentFilter) {
        checkNotNull(context, "context == null");
        checkNotNull(intentFilter, "intentFilter == null");
        return Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Intent> subscriber) throws Exception {
                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override public void onReceive(Context context, Intent intent) {
                        subscriber.onNext(intent);
                    }
                };

                context.registerReceiver(receiver, intentFilter);

                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        context.unregisterReceiver(receiver);
                    }
                });
            }
        });
    }
}
