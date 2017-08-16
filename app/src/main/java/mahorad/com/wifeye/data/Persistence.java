package mahorad.com.wifeye.data;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mahorad.com.wifeye.publisher.event.persistence.RxPersistenceMonitor;

import static mahorad.com.wifeye.util.Utils.isNullOrEmpty;

/**
 * Created by mahan on 2017-08-14.
 */

public class Persistence {

    public static final String TAG = Persistence.class.getSimpleName();

    private static final HashMap<String, Set<String>> db = new HashMap<>();

    static {
        addFakeValues();
    }

    private static void addFakeValues() {
        HashSet<String> towers = new HashSet<String>() {{
            add("[20201,13238893,-1]");
            add("[20101,13302242,-1]");
            add("[20201,13302242,-1]");
            add("[20201,13292922,-1]");
            add("[20101,13173328,-1]");
            add("[2011,134415638,-1]");
            add("[2021,582420,-1]");
            add("[20201,13173317,-1]");
        }};
        db.put("Archer", towers);
    }

    public static void persist(String ssid, final String ctid) {
        if (!isValid(ssid)) return;
        if (!isValid(ctid)) return;
        if (exist(ssid, ctid)) return;
        doPersist(ssid, ctid);
    }

    private static boolean isValid(String string) {
        if (isNullOrEmpty(string))
            return false;
        return !string.contains("n/a");
    }

    private static void doPersist(String ssid, final String ctid) {
        if (!db.containsKey(ssid)) {
            db.put(ssid, new HashSet<String>() {{
                add(ctid);
            }});
            RxPersistenceMonitor.notify(ssid, ctid);
            Log.d(TAG, String.format("PERSISTED CTID %s -> SSID %s", ctid, ssid));
        } else {
            db.get(ssid).add(ctid);
            RxPersistenceMonitor.notify(ssid, ctid);
        }
    }

    public static Set<String> towersOf(String ssid) {
        if (isNullOrEmpty(ssid))
            return new HashSet<>();
        if (!db.containsKey(ssid))
            return new HashSet<>();
        return db.get(ssid);
    }

    public static boolean exist(String ssid, String ctid) {
        if (!db.containsKey(ssid))
            return false;
        return db.get(ssid).contains(ctid);
    }

    public static boolean exist(String ctid) {
        for (Set<String> set : db.values()) {
            if (set.contains(ctid))
                return true;
        }
        return false;
    }


}
