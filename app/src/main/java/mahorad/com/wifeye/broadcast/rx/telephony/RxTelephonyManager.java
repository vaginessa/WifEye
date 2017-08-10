package mahorad.com.wifeye.broadcast.rx.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import io.reactivex.Observable;
import mahorad.com.wifeye.broadcast.rx.RxBroadcastReceiver;

import static dagger.internal.Preconditions.checkNotNull;

public final class RxTelephonyManager {

    private RxTelephonyManager() {
        throw new AssertionError("no instances");
    }

    @CheckResult
    @NonNull
    public static Observable<PhoneStateChangedEvent> phoneStateChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        return RxBroadcastReceiver
                .create(context, filter)
                .map(intent -> {
                    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    return PhoneStateChangedEvent.create(state, phoneNumber);
                });
    }

    @CheckResult
    @NonNull
    public static Observable<CellLocation> cellLocationChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return Observable.create(e -> {
            final PhoneStateListener phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCellLocationChanged(CellLocation location) {
                    super.onCellLocationChanged(location);
                    e.onNext(location);
                }
            };
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);
            e.setCancellable(() -> telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE));
        });
    }
}