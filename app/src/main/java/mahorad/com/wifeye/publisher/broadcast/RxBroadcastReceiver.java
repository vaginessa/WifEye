package mahorad.com.wifeye.publisher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import static dagger.internal.Preconditions.checkNotNull;

public final class RxBroadcastReceiver {

    private RxBroadcastReceiver() {
        throw new AssertionError("no instances");
    }

    @CheckResult
    @NonNull
    public static Observable<Intent> create(
            @NonNull final Context context,
            @NonNull final IntentFilter intentFilter)
    {
        checkNotNull(context, "context == null");
        checkNotNull(intentFilter, "intentFilter == null");
        return Observable.create(emitter ->
                create(context, intentFilter, emitter)
        );
    }

    private static void create(
            @NonNull final Context context,
            @NonNull final IntentFilter filter,
            @NonNull final ObservableEmitter<? super Intent> emitter)
    {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                emitter.onNext(i);
            }
        };
        context.registerReceiver(receiver, filter);
        emitter.setCancellable(() ->
                context.unregisterReceiver(receiver)
        );
    }
}