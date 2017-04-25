package wifeye.app.android.mahorad.com.wifeye.persist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BasePersistence implements IPersistence {

    private final HashMap<String, Set<String>> db = new HashMap<>();

    @Override
    public void persist(String ssid, final String ctid) {
        if (!db.containsKey(ssid)) {
            db.put(ssid, new HashSet<String>() {{
                add(ctid);
            }});
        } else {
            db.get(ssid).add(ctid);
        }
    }

    @Override
    public boolean exist(String ssid, String ctid) {
        if (!db.containsKey(ssid)) return false;
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
