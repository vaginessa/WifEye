package mahorad.com.wifeye.engine;

interface IActuator {

    void disableWifi();

    void observeWifi();

    void haltWifiActions();

    void persist();
}
