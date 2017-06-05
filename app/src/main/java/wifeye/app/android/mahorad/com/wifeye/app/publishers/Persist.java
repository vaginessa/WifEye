package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IPersistListener;

public class Persist {

    private Set<IPersistListener> consumers = new HashSet<>();
    private static Date date = getTime();
    private static String data = "-";

    public void setData(final String data) {
        Persist.data = data;
        Persist.date = getTime();
        publish(data);
    }

    private static Date getTime() {
        return Calendar.getInstance().getTime();
    }

    private void publish(String data) {
        synchronized (this) {
            for (IPersistListener consumer : consumers) {
                Executors.newSingleThreadExecutor()
                        .submit(() -> consumer.onDataPersisted(data));
            }
        }
    }

    public boolean subscribe(IPersistListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IPersistListener consumer) {
        return consumers.remove(consumer);
    }

    public static String data() {
        return data;
    }

    public static Date date() {
        return date;
    }
}