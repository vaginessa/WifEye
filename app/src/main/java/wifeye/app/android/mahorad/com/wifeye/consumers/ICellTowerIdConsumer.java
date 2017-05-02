package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface ICellTowerIdConsumer {

    void onReceivedKnownTowerId(String ctid);

    void onReceivedUnknownTowerId(String ctid);

}
