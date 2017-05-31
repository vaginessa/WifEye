package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;
import wifeye.app.android.mahorad.com.wifeye.app.state.State;

public class SystemStatePublisher {

    private static IState.Type state = IState.Type.Initial;
    private static Date date = Calendar.getInstance().getTime();
    private final Set<ISystemStateConsumer> consumers;

    public SystemStatePublisher() {
        consumers = new HashSet<>();
    }

    public void publish(final IState state) {
        SystemStatePublisher.state = state.type();
        date = Calendar.getInstance().getTime();
        for (final ISystemStateConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onStateChanged(state.type()));
        }
    }

    public boolean subscribe(ISystemStateConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(ISystemStateConsumer consumer) {
        return consumers.remove(consumer);
    }

    public IState.Type state() {
        return state;
    }

    public Date date() {
        return date;
    }
}
