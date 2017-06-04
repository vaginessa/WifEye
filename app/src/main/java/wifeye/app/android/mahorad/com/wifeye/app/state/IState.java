package wifeye.app.android.mahorad.com.wifeye.app.state;

public interface IState {

    enum Type {

        Initial("Initial"),
        Connected("router reach"),
        DisConnected("disconnected"),
        UnknownArea("unknown region"),
        KnownArea("nearby area"),
        RouterArea("router reach");

        private final String title;

        Type(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    Type type();

    void onInternetConnected();

    void onInternetDisconnects();

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId();
    
}
