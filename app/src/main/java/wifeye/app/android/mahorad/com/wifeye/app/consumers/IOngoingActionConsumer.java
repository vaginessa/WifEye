package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;

public interface IOngoingActionConsumer {

    void onActionChanged(Action action);

}
