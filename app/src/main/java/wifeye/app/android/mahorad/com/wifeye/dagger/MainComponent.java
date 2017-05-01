package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.content.Context;

import javax.inject.Singleton;

import wifeye.app.android.mahorad.com.wifeye.consumers.SsidTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

@Singleton
@dagger.Component(modules = MainModule.class)
public interface MainComponent {

    Context context();

    Utilities utilities();

    OngoingActionPublisher actionPublisher();

    SystemStatePublisher statePublisher();

    CellTowerIdPublisher ctidPublisher();

    WifiSsidNamePublisher ssidPublisher();

    SsidTowerIdConsumer ssidctidConsumer();

}
