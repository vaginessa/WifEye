package wifeye.app.android.mahorad.com.wifeye.app.persist;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.PersistencePublisher;

public class MemoryPersistence extends Persistence {

    private static final String TAG = MemoryPersistence.class.getSimpleName();

    private final HashMap<String, Set<String>> db = new HashMap<>();

    public MemoryPersistence(PersistencePublisher publisher) {
        super(publisher);
    }

    @Override
    public void persist(String ssid, final String ctid) {
        if (ssid == null || ctid == null)
            return;
        if (!db.containsKey(ssid)) {
            db.put(ssid, new HashSet<String>() {{
                add(ctid);
            }});
            publisher.publishDataPersisted();
            Log.d(TAG, String.format("PERSISTED CTID %s -> SSID %s", ctid, ssid));
        } else {
            db.get(ssid).add(ctid);
            publisher.publishDataPersisted();
        }
    }

    @Override
    public boolean exist(String ssid, String ctid) {
        if (!db.containsKey(ssid))
            return false;
        return db.get(ssid).contains(ctid);
    }

    @Override
    public boolean exist(String ctid) {
        for (Set<String> set : db.values()) {
            if (set.contains(ctid))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String message = "";
        for (String ssid : db.keySet()) {
            message += ssid + "\n";
            Set<String> ctids = db.get(ssid);
            for (String ctid : ctids) {
                message += "  " + ctid + "\n";
            }
        }
        return message;
    }
}
