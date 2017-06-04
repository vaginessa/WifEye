package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IEngineListener;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;

public class Engine {

    private static IState.Type state = IState.Type.Initial;
    private static Date date = Calendar.getInstance().getTime();
    private final Set<IEngineListener> consumers;

    public Engine() {
        consumers = new HashSet<>();
    }

    public void publish(final IState state) {
        Engine.state = state.type();
        date = Calendar.getInstance().getTime();
        for (final IEngineListener consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onStateChanged(state.type()));
        }
    }

    public boolean subscribe(IEngineListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IEngineListener consumer) {
        return consumers.remove(consumer);
    }

    public IState.Type state() {
        return state;
    }

    public Date date() {
        return date;
    }
}
