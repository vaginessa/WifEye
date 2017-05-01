package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.state.IState;

public class SystemStatePublisher {

    private final List<ISystemStateConsumer> consumers;

    public SystemStatePublisher() {
        consumers = new ArrayList<>();
    }

    public void publish(final IState state) {
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
}
