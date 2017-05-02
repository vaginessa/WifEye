package wifeye.app.android.mahorad.com.wifeye.consumers;

import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action;

public interface IOngoingActionConsumer {

    void onActionChanged(Action action);

}
