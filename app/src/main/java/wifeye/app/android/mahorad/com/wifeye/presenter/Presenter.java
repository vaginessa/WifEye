package wifeye.app.android.mahorad.com.wifeye.presenter;

import android.content.Context;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IWifiDeviceStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IWifiSsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiDeviceStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.state.IState;
import wifeye.app.android.mahorad.com.wifeye.ui.view.IViewSummary;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;

public class Presenter implements
        IPresenter,
        ICellTowerIdConsumer,
        ISystemStateConsumer,
        IWifiDeviceStateConsumer,
        IWifiSsidNameConsumer {

    private final IViewSummary view;

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
    public Presenter(IViewSummary view) {
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
        String date = ssidPublisher.date();
        view.updateHotspotState(ssid, date);
    }

    private void updateTowerIdState() {
        String ctid = ctidPublisher.ctid();
        String date = (ctid == null || ctid.equals(""))
                ? "" : ctidPublisher.date();
        view.updateTowerIdState(ctid, date);
    }

    private void updateEngineState() {
        String state = statePublisher.state();
        String date = statePublisher.date();
        view.updateEngineState(state, date);
    }

    private void updateWifiDeviceState() {
        view.updateWifiDeviceState(wifiPublisher.state());
    }

    @Override
    public void onInternetConnected(String ssid) {
        view.updateHotspotState(ssid, utils.simpleDate());
    }

    @Override
    public void onInternetDisconnected() {
        view.updateHotspotState(null, utils.simpleDate());
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        view.updateTowerIdState(ctid, utils.simpleDate());
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        view.updateTowerIdState(ctid, utils.simpleDate());
    }

    @Override
    public void onWifiStateChanged(WifiState state) {
        view.updateWifiDeviceState(state);
    }

    @Override
    public void onStateChanged(IState state) {
        view.updateEngineState(state.toString(), utils.simpleDate());
    }

}
