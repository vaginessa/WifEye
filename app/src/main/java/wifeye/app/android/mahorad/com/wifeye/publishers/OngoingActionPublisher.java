package wifeye.app.android.mahorad.com.wifeye.publishers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;

import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.Disabling;
import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.None;
import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action.ObserveModeEnabling;

public class OngoingActionPublisher {

    private List<IOngoingActionConsumer> consumers = new ArrayList<>();

    Action ongoingAction = Action.None;

    public enum Action {
        None, Disabling, ObserveModeDisabling, ObserveModeEnabling
    }

    public void publishDisabling() {
        synchronized (this) {
            ongoingAction = Disabling;
            publishOngoingAction();
        }
    }

    public void publishObserveModeDisabling() {
        synchronized (this) {
            ongoingAction = ObserveModeDisabling;
            publishOngoingAction();
        }
    }

    public void publishObserveModeEnabling() {
        synchronized (this) {
            ongoingAction = ObserveModeEnabling;
            publishOngoingAction();
        }
    }

    public void publishHalt() {
        synchronized (this) {
            ongoingAction = None;
            publishOngoingAction();
        }
    }

    private void publishOngoingAction() {
        for (IOngoingActionConsumer consumer : consumers) {
            Executors.newSingleThreadExecutor()
                    .submit(() -> consumer.onActionChanged(ongoingAction));
        }
    }

    public boolean subscribe(IOngoingActionConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IOngoingActionConsumer consumer) {
        return consumers.remove(consumer);
    }

    public Action ongoingAction() {
        return ongoingAction;
    }
}
