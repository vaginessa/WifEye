package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;

public class OngoingActionPublisher {

    private List<IOngoingActionConsumer> consumers = new ArrayList<>();

    public void publishDisabling() {
        synchronized (this) {
            for (IOngoingActionConsumer consumer : consumers) {
                Executors
                        .newSingleThreadExecutor()
                        .submit(consumer::onDisabling);
            }

        }
    }

    public void publishObserveModeDisabling() {
        synchronized (this) {
            for (IOngoingActionConsumer consumer : consumers) {
                Executors
                        .newSingleThreadExecutor()
                        .submit(consumer::onObserveModeDisabling);
            }
        }
    }

    public void publishObserveModeEnabling() {
        synchronized (this) {
            for (IOngoingActionConsumer consumer : consumers) {
                Executors
                        .newSingleThreadExecutor()
                        .submit(consumer::onObserveModeEnabling);
            }
        }
    }

    public void publishHalt() {
        synchronized (this) {
            for (IOngoingActionConsumer consumer : consumers) {
                Executors
                        .newSingleThreadExecutor()
                        .submit(consumer::onHalted);
            }
        }
    }

    public boolean subscribe(IOngoingActionConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IOngoingActionConsumer consumer) {
        return consumers.remove(consumer);
    }

}
