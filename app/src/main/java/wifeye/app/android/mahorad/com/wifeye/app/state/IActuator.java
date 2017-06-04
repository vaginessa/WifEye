package wifeye.app.android.mahorad.com.wifeye.app.state;

interface IActuator {

    void start();

    void stop();

    void disableWifi();

    void observeWifi();

    void haltWifiAct();

    void persist();
}
