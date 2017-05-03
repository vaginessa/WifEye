package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.consumers.IPersistenceConsumer;

public class PersistencePublisher {

    private List<IPersistenceConsumer> consumers = new ArrayList<>();

    public void publishDataPersisted() {
        synchronized (this) {
            for (IPersistenceConsumer consumer : consumers) {
                Executors.newSingleThreadExecutor()
                        .submit(consumer::onDataPersisted);
            }
        }
    }

    public boolean subscribe(IPersistenceConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IPersistenceConsumer consumer) {
        return consumers.remove(consumer);
    }

}