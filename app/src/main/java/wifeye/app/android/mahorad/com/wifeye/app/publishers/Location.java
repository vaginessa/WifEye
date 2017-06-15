package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.Context;
import android.telephony.CellLocation;

import java.util.Date;

import io.reactivex.Observable;
import wifeye.app.android.mahorad.com.wifeye.app.events.LocationEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.telephony.RxTelephonyManager;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

/**
 * listens to receiving cell tower identifiers and
 * notifies the consumers.
 */
public class Location {

    private static String ctid = "n/a";
    private static boolean known;
    private static Date date = Utilities.now();

    public static Observable<LocationEvent> observable(Context context) {
        return RxTelephonyManager
                .cellLocationChanges(context)
                .distinctUntilChanged()
                .map(Location::toEvent);
    }

    private static LocationEvent toEvent(CellLocation location) {
        ctid = (location == null ? "" : location.toString());
        known = Utilities.isNullOrEmpty(ctid) ||
                (Internet.connected()
                        ? Persistence.exist(Internet.ssid(), ctid)
                        : Persistence.exist(ctid));
        date = Utilities.now();
        return LocationEvent.create(ctid, known, date);
    }

    public static String ctid() {
        return ctid;
    }

    public static boolean known() {
        return known;
    }

    public static Date date() {
        return date;
    }

    public static LocationEvent lastEvent() {
        return LocationEvent.create(ctid, known, date);
    }

}
