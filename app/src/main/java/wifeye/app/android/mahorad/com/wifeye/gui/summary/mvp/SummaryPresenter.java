package wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp;

import android.content.Context;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class SummaryPresenter implements IPresenter {

    private final ISummaryView view;

    @Inject @ApplicationContext Context context;
    @Inject Utilities utils;
    @Inject
    Internet ssidPublisher;
    @Inject CellTowerIdPublisher ctidPublisher;
    @Inject SystemStatePublisher statePublisher;
    @Inject
    Wifi wifiPublisher;
    @Inject IPersistence persistence;

    /**
     *
     * @param view
     */
    public SummaryPresenter(ISummaryView view) {
        this.view = view;
        MainApplication
                .mainComponent()
                .inject(this);
        subscribe();
    }

    private void subscribe() {
    }

    @Override
    public void onCreate() {}

    @Override
    public void onPause() {}

    @Override
    public void onResume() { updateViewStates(); }

    @Override
    public void onDestroy() {}

    private void updateViewStates() {
    }
}
