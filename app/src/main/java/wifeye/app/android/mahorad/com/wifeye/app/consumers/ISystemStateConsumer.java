package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import wifeye.app.android.mahorad.com.wifeye.app.state.IState;

public interface ISystemStateConsumer {

    void onStateChanged(IState state);

}
