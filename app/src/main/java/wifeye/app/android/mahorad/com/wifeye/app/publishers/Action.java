package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IActionListener;

public class Action {

    private Set<IActionListener> consumers = new HashSet<>();
    private static State action = State.Halt;
    private static Date date = Calendar.getInstance().getTime();

    public enum State {

        Halt("Idle"),
        DisablingMode("Disabling"),
        ObserveModeDisabling("..disabling.."),
        ObserveModeEnabling("..enabling..");

        private final String title;

        State(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }


    public synchronized void publish(State wifiAction) {
        action = wifiAction;
        date = Calendar.getInstance().getTime();
        publishOngoingAction();
    }

    private void publishOngoingAction() {
        for (IActionListener consumer : consumers) {
            Executors.newSingleThreadExecutor()
                    .submit(() -> consumer.onActionChanged(action));
        }
    }

    public boolean subscribe(IActionListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IActionListener consumer) {
        return consumers.remove(consumer);
    }

    public State action() {
        return action;
    }

    public Date date() { return date; }
}
