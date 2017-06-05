package wifeye.app.android.mahorad.com.wifeye.app.persist;

public interface IPersistence {

    void persist(String ssid, String ctid);

    boolean exist(String ssid, String ctid);

}
