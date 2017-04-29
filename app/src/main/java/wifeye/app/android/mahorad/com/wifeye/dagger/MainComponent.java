package wifeye.app.android.mahorad.com.wifeye.dagger;

import javax.inject.Singleton;

import wifeye.app.android.mahorad.com.wifeye.consumers.SsidAndCellConsumer;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;

@Singleton
@dagger.Component(modules = MainModule.class)
public interface MainComponent {

    SystemStatePublisher statePublisher();

    CellTowerPublisher towerPublisher();

    BssidNamePublisher bssidPublisher();

    SsidAndCellConsumer ssidCellConsumer();

}
