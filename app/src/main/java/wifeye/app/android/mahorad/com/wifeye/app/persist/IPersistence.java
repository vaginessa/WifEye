package wifeye.app.android.mahorad.com.wifeye.app.persist;

import java.util.Set;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IPersistListener;

public interface IPersistence {

    void persist(String ssid, String ctid);

    boolean exist(String ssid, String ctid);

    boolean exist(String ctid);

    Set<String> towersOf(String ssid);

    boolean subscribe(IPersistListener consumer);

    boolean unsubscribe(IPersistListener consumer);



}
