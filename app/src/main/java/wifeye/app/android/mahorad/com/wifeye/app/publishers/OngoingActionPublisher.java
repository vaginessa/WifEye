package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IOngoingActionConsumer;

public class OngoingActionPublisher {

    private Set<IOngoingActionConsumer> consumers = new HashSet<>();
    private static Action action = Action.Halt;
    private static Date date = Calendar.getInstance().getTime();

    public synchronized void publish(Action wifiAction) {
        action = wifiAction;
        date = Calendar.getInstance().getTime();
        publishOngoingAction();
    }

    private void publishOngoingAction() {
        for (IOngoingActionConsumer consumer : consumers) {
            Executors.newSingleThreadExecutor()
                    .submit(() -> consumer.onActionChanged(action));
        }
    }

    public boolean subscribe(IOngoingActionConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IOngoingActionConsumer consumer) {
        return consumers.remove(consumer);
    }

    public Action action() {
        return action;
    }

    public Date date() { return date; }
}
