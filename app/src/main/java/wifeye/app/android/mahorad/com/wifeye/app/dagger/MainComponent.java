package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Location;
import wifeye.app.android.mahorad.com.wifeye.app.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.gui.views.ActionView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.InternetView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.PersistView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.LocationView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.EngineView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.WifiView;

@ApplicationScope
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(Engine engine);

    void inject(MainService mainService);

    void inject(LocationView locationView);

    void inject(ActionView actionView);

    void inject(EngineView engineView);

    void inject(InternetView internetView);

    void inject(WifiView wifiView);

    void inject(PersistView persistView);

    Action actionPublisher();

    Location ctidPublisher();

    Internet ssidPublisher();

    Utilities utilities();

    Engine engine();

}
