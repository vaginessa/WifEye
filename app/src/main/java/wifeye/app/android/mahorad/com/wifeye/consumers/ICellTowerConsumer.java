package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface ICellTowerConsumer {

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId(String ctid);

}
