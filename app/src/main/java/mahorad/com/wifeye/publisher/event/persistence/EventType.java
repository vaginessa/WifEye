package mahorad.com.wifeye.publisher.event.persistence;

/**
 * Created by mahan on 2017-08-18.
 */

public enum EventType {

    WifiState("Wifi State"),
    WifiAction("Wifi Action"),
    Internet("Internet"),
    CellTower("Cell Tower"),
    EngineState("Engine State"),
    Persistence("Persistence");

    private final String title;

    EventType(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

}
