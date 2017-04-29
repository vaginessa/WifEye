package wifeye.app.android.mahorad.com.wifeye.publishers.state;

interface IActuator {

    void disableWifi();

    void standbyWifi();

    void halt();

    void persist();
}
