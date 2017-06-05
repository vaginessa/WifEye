package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Signal;
import wifeye.app.android.mahorad.com.wifeye.app.state.StateMachine;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persist;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.gui.views.ActionView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.HotspotView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.PersistView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.SignalView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.EngineView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.WifiView;

@ApplicationScope
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(StateMachine stateMachine);

    void inject(MainService mainService);

    void inject(SignalView signalView);

    void inject(ActionView actionView);

    void inject(EngineView engineView);

    void inject(HotspotView hotspotView);

    void inject(WifiView wifiView);

    void inject(PersistView persistView);

    Action actionPublisher();

    Engine statePublisher();

    Signal ctidPublisher();

    Internet ssidPublisher();

    Persist repoPublisher();

    Utilities utilities();

    IPersistence persistence();

    StateMachine engine();

}
