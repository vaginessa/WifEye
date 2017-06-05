package wifeye.app.android.mahorad.com.wifeye.app.persist;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persist;

public class MemoryPersistence extends Persistence {

    private static final String TAG = MemoryPersistence.class.getSimpleName();

    private final HashMap<String, Set<String>> db = new HashMap<>();

    public MemoryPersistence(Persist publisher) {
        super(publisher);
//        addFakeValues();
    }

//    private void addFakeValues() {
//        HashSet<String> towers = new HashSet<String>() {{
//            add("[20201,13238893,-1]");
//            add("[20101,13302242,-1]");
//            add("[20201,13302242,-1]");
//            add("[20201,13292922,-1]");
//            add("[20101,13173328,-1]");
//            add("[2011,134415638,-1]");
//            add("[2021,582420,-1]");
//            add("[20201,13173317,-1]");
//        }};
//        db.put("Archer", towers);
//    }

    @Override
    public void persist(String ssid, final String ctid) {
        if (nullOrEmpty(ssid)) return;
        if (nullOrEmpty(ctid)) return;
        if (exist(ssid, ctid)) return;
        doPersist(ssid, ctid);
    }

    private boolean nullOrEmpty(String text) {
        if (text == null) return true;
        if (text.length() == 0)
            return true;
        return text.replace(" ", "").length() == 0;
    }

    private void doPersist(String ssid, final String ctid) {
        if (!db.containsKey(ssid)) {
            db.put(ssid, new HashSet<String>() {{
                add(ctid);
            }});
            persist.setData(format(ssid));
            Log.d(TAG, String.format("PERSISTED CTID %s -> SSID %s", ctid, ssid));
        } else {
            db.get(ssid).add(ctid);
            persist.setData(format(ssid));
        }
    }

    private String format(String ssid) {
        int towers = towersCount(ssid);
        String plural = towers > 1 ? "s" : "";
        return String.format("%s: %d Tower%s", ssid, towers, plural);
    }

    private int towersCount(String ssid) {
        return towersOf(ssid).size();
    }

    private Set<String> towersOf(String ssid) {
        if (nullOrEmpty(ssid))
            return new HashSet<>();
        if (!db.containsKey(ssid))
            return new HashSet<>();
        return db.get(ssid);
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
