package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IActionListener;

import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.Halt;

public class Action {

    private Set<IActionListener> consumers = new HashSet<>();
    private static Type type = Halt;
    private static Date date = Calendar.getInstance().getTime();

    public enum Type {

        Halt("Idle"),
        DisablingMode("Disabling"),
        ObserveModeDisabling("..disabling.."),
        ObserveModeEnabling("..enabling..");

        private final String title;

        Type(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    public synchronized void setType(Type type) {
        if (Action.type == type) return;
        Action.type = type;
        date = Calendar.getInstance().getTime();
        publish();
    }

    private void publish() {
        for (IActionListener consumer : consumers) {
            Executors.newSingleThreadExecutor()
                    .submit(() -> consumer.onActionChanged(type));
        }
    }

    public boolean subscribe(IActionListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IActionListener consumer) {
        return consumers.remove(consumer);
    }

    public static Type action() {
        return type;
    }

    public static Date date() { return date; }
}
