package wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp;

import android.content.Context;

import java.util.Date;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiDeviceStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiSsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiDeviceStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class SummaryPresenter implements
        IPresenter,
        ICellTowerIdConsumer,
        ISystemStateConsumer,
        IWifiDeviceStateConsumer,
        IWifiSsidNameConsumer {

    private final ISummaryView view;

    @Inject @ApplicationContext Context context;
    @Inject Utilities utils;
    @Inject WifiSsidNamePublisher ssidPublisher;
    @Inject CellTowerIdPublisher ctidPublisher;
    @Inject SystemStatePublisher statePublisher;
    @Inject WifiDeviceStatePublisher wifiPublisher;
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
        ssidPublisher.subscribe(this);
        ctidPublisher.subscribe(this);
        statePublisher.subscribe(this);
        wifiPublisher.subscribe(this);
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
        updateHotspotState();
        updateTowerIdState();
        updateEngineState();
        updateWifiDeviceState();
    }

    private void updateHotspotState() {
        String ssid = ssidPublisher.ssid();
        Date date = ssidPublisher.date();
        view.updateHotspotState(ssid, date);
    }

    private void updateTowerIdState() {
        String ctid = ctidPublisher.ctid();
        Date date = ctidPublisher.date();
        view.updateTowerIdState(ctid, date);
    }

    private void updateEngineState() {
        String state = statePublisher.state();
        Date date = statePublisher.date();
        view.updateEngineState(state, date);
    }

    private void updateWifiDeviceState() {
        view.updateWifiDeviceState(wifiPublisher.state());
    }

    @Override
    public void onInternetConnected(String ssid) {
    }

    @Override
    public void onInternetDisconnected() {
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
    }

    @Override
    public void onWifiStateChanged(WifiState state) {
    }

    @Override
    public void onStateChanged(IState state) {
    }

}
