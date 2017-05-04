package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.state.IState;

public class SystemStatePublisher {

    private static String state;
    private static String date;
    private final Set<ISystemStateConsumer> consumers;

    public SystemStatePublisher() {
        consumers = new HashSet<>();
    }

    public void publish(final IState state) {
        this.state = state.toString();
        date = MainApplication
                .mainComponent()
                .utilities()
                .simpleDate();
        for (final ISystemStateConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onStateChanged(state));
        }
    }

    public boolean subscribe(ISystemStateConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(ISystemStateConsumer consumer) {
        return consumers.remove(consumer);
    }

    public String state() {
        return state;
    }

    public String date() {
        return date;
    }
}
