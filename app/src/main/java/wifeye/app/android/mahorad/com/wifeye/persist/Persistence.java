package wifeye.app.android.mahorad.com.wifeye.persist;

import wifeye.app.android.mahorad.com.wifeye.publishers.PersistencePublisher;

public abstract class Persistence implements IPersistence {

    protected final PersistencePublisher publisher;

    public Persistence(PersistencePublisher publisher) {
        this.publisher = publisher;
    }

}
