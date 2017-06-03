package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp.SummaryPresenter;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.PersistencePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.gui.views.ActionView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.BssidView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.SignalView;
import wifeye.app.android.mahorad.com.wifeye.gui.views.StateView;

@ApplicationScope
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(SummaryPresenter presenter);

    void inject(MainService mainService);

    void inject(SignalView signalView);

    void inject(ActionView actionView);

    void inject(StateView stateView);

    void inject(BssidView bssidView);

    OngoingActionPublisher actionPublisher();

    SystemStatePublisher statePublisher();

    CellTowerIdPublisher ctidPublisher();

    WifiSsidNamePublisher ssidPublisher();

    PersistencePublisher repoPublisher();

    Utilities utilities();

    IPersistence persistence();
}
