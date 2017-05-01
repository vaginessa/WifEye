package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface ICellTowerIdConsumer {

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId(String ctid);

}
