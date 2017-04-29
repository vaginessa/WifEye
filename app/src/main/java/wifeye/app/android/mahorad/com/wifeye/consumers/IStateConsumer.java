package wifeye.app.android.mahorad.com.wifeye.consumers;

import wifeye.app.android.mahorad.com.wifeye.publishers.state.IState;

public interface IStateConsumer {

    void onStateChanged(IState state);

}
