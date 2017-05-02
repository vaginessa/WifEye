package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.Disabling;
import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.None;
import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.ObserveModeEnabling;

public class OngoingActionPublisher {

    private List<IOngoingActionConsumer> consumers = new ArrayList<>();

    private static Action action = Action.None;
    private static String date;
    private Utilities utils =
            MainApplication
                    .mainComponent()
                    .utilities();

    public enum Action {
        None, Disabling, ObserveModeDisabling, ObserveModeEnabling
    }

    public void publishDisabling() {
        synchronized (this) {
            action = Disabling;
            date = utils.simpleDate();
            publishOngoingAction();
        }
    }

    public void publishObserveModeDisabling() {
        synchronized (this) {
            action = ObserveModeDisabling;
            publishOngoingAction();
        }
    }

    public void publishObserveModeEnabling() {
        synchronized (this) {
            action = ObserveModeEnabling;
            publishOngoingAction();
        }
    }

    public void publishHalt() {
        synchronized (this) {
            action = None;
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
