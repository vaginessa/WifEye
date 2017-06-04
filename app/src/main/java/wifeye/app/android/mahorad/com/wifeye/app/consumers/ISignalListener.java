package wifeye.app.android.mahorad.com.wifeye.app.consumers;

public interface ISignalListener {

    void onReceivedKnownTowerId(String ctid);

    void onReceivedUnknownTowerId(String ctid);

}
