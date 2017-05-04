package wifeye.app.android.mahorad.com.wifeye.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.MainActivity;
import wifeye.app.android.mahorad.com.wifeye.MainService;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.presenter.Presenter;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.PersistencePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

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
