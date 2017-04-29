package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.ArrayList;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.state.IState;

public class SystemStatePublisher {

    private final List<ISystemStateConsumer> consumers;

    public SystemStatePublisher() {
        consumers = new ArrayList<>();
    }

    public void publish(final IState state) {
        for (final ISystemStateConsumer consumer : consumers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    consumer.onStateChanged(state);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public boolean subscribe(ISystemStateConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(ISystemStateConsumer consumer) {
        return consumers.remove(consumer);
    }
}
