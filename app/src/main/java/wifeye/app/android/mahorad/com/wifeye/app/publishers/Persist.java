package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IPersistListener;

public class Persist {

    private Set<IPersistListener> consumers = new HashSet<>();

    public void publishDataPersisted() {
        synchronized (this) {
            for (IPersistListener consumer : consumers) {
                Executors.newSingleThreadExecutor()
                        .submit(consumer::onDataPersisted);
            }
        }
    }

    public boolean subscribe(IPersistListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IPersistListener consumer) {
        return consumers.remove(consumer);
    }

}
