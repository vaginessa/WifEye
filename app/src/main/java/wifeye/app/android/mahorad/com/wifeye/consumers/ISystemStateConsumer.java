package wifeye.app.android.mahorad.com.wifeye.consumers;

import wifeye.app.android.mahorad.com.wifeye.state.IState;

public interface ISystemStateConsumer {

    void onStateChanged(IState state);

}
