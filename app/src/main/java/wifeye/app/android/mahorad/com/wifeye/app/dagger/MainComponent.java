package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.gui.MainActivity;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp.Presenter;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.PersistencePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

@ApplicationScope
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(Presenter presenter);

    void inject(MainService mainService);

    OngoingActionPublisher actionPublisher();

    SystemStatePublisher statePublisher();

    CellTowerIdPublisher ctidPublisher();

    WifiSsidNamePublisher ssidPublisher();

    PersistencePublisher repoPublisher();

    Utilities utilities();

    IPersistence persistence();

}
