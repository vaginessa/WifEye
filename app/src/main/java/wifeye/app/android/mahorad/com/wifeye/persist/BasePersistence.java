package wifeye.app.android.mahorad.com.wifeye.persist;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BasePersistence implements IPersistence {

    private static final String TAG = BasePersistence.class.getSimpleName();

    private final HashMap<String, Set<String>> db = new HashMap<>();

    @Override
    public void persist(String ssid, final String ctid) {
        if (ssid == null || ctid == null)
            return;
        if (!db.containsKey(ssid)) {
            db.put(ssid, new HashSet<String>() {{
                add(ctid);
            }});
            Log.d(TAG, String.format("PERSISTED CTID %s -> SSID %s", ctid, ssid));
        } else {
            db.get(ssid).add(ctid);
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
}
