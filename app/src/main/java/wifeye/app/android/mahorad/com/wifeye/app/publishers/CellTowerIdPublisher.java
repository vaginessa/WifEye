package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;

/**
 * listens to receiving cell tower identifiers and
 * notifies the consumers.
 */
public class CellTowerIdPublisher extends PhoneStateListener {

    private static String ctid;
    private static Date date = Calendar.getInstance().getTime();
    private final Set<ICellTowerIdConsumer> consumers;
    private final Context context;
    private final IPersistence persistence;

    public CellTowerIdPublisher(Context context, IPersistence persistence) {
        this.context = context;
        consumers = new HashSet<>();
        this.persistence = persistence;
    }

    @Override
    public void onCellLocationChanged(CellLocation cellLocation) {
        super.onCellLocationChanged(cellLocation);
        synchronized (this) {
            String towerId = cellLocation.toString();
            if (isSame(towerId)) return;
            ctid = towerId;
            date = Calendar.getInstance().getTime();
            if (persistence.exist(ctid))
                publishReceivedKnownTowerId();
            else
                publishReceivedUnknownTowerId();
        }
    }

    private boolean isSame(String text) {
        if (text == null && ctid == null) return true;
        boolean anyNull = (ctid == null || text == null);
        return !anyNull && ctid.equals(text);
    }

    private void publishReceivedKnownTowerId() {
        for (final ICellTowerIdConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onReceivedKnownTowerId(ctid));
        }
    }

    private void publishReceivedUnknownTowerId() {
        for (final ICellTowerIdConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onReceivedUnknownTowerId(ctid));
        }
    }

    public void start() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    public void stop() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
        consumers.clear();
    }

    public boolean subscribe(ICellTowerIdConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(ICellTowerIdConsumer consumer) {
        return consumers.remove(consumer);
    }

    public String ctid() {
        return ctid;
    }

    public Date date() {
        return date;
    }

}
