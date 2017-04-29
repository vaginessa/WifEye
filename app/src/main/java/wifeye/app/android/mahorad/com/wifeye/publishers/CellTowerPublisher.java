package wifeye.app.android.mahorad.com.wifeye.publishers;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.consumers.ICellTowerConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;

/**
 * listens to receiving cell tower identifiers and
 * notifies the consumers.
 */
public class CellTowerPublisher extends PhoneStateListener {

    private static String ctid;
    private final List<ICellTowerConsumer> consumers;
    private final Context context;
    private final IPersistence persistence;

    public CellTowerPublisher(Context context, IPersistence persistence) {
        this.context = context;
        consumers = new ArrayList<>();
        this.persistence = persistence;
    }

    @Override
    public void onCellLocationChanged(CellLocation cellLocation) {
        super.onCellLocationChanged(cellLocation);
        synchronized (this) {
            String towerId = cellLocation.toString();
            if (isSame(towerId)) return;
            ctid = towerId;
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
        for (final ICellTowerConsumer consumer : consumers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    consumer.onReceivedKnownTowerId();
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void publishReceivedUnknownTowerId() {
        for (final ICellTowerConsumer consumer : consumers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    consumer.onReceivedUnknownTowerId(ctid);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void start() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    public void stop() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
    }

    public boolean subscribe(ICellTowerConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(ICellTowerConsumer consumer) {
        return consumers.remove(consumer);
    }

    public static String ctid() {
        return ctid;
    }
}
