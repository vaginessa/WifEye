package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface ITowerConsumer {

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId(String ctid);

}
