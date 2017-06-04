package wifeye.app.android.mahorad.com.wifeye.app.persist;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persist;

public abstract class Persistence implements IPersistence {

    protected final Persist publisher;

    public Persistence(Persist publisher) {
        this.publisher = publisher;
    }

}
