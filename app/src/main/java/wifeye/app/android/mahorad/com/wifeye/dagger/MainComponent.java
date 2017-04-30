package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.content.Context;

import javax.inject.Singleton;

import wifeye.app.android.mahorad.com.wifeye.consumers.SsidAndCellConsumer;
import wifeye.app.android.mahorad.com.wifeye.publishers.BssidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

@Singleton
@dagger.Component(modules = MainModule.class)
public interface MainComponent {

    Context context();

    Utilities utilities();

    SystemStatePublisher statePublisher();

    CellTowerPublisher ctidPublisher();

    BssidNamePublisher ssidPublisher();

    SsidAndCellConsumer ssidctidConsumer();

}
