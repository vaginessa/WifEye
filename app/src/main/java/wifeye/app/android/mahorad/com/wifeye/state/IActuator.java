package wifeye.app.android.mahorad.com.wifeye.state;

interface IActuator {

    void disableWifi();

    void observeWifi();

    void haltWifiAct();

    void persist();
}
