package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

public class OngoingActionPublisher {

    private Set<IOngoingActionConsumer> consumers = new HashSet<>();
    private static Action action = Action.Halt;
    private static String date;

    private Utilities utils =
            MainApplication
                    .mainComponent()
                    .utilities();

    public void publish(Action wifiAction) {
        synchronized (this) {
            action = wifiAction;
            date = utils.simpleDate();
            publishOngoingAction();
        }
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

    public String date() { return date; }
}
