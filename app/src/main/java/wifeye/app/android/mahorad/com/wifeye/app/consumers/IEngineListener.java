package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import wifeye.app.android.mahorad.com.wifeye.app.state.State;

public interface IEngineListener {

    void onStateChanged(State.Type state);

}
