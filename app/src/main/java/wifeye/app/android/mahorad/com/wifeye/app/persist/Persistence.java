package wifeye.app.android.mahorad.com.wifeye.app.persist;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persist;

public abstract class Persistence implements IPersistence {

    protected final Persist persist;

    public Persistence(Persist persist) {
        this.persist = persist;
    }

}
