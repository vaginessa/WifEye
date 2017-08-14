package mahorad.com.wifeye.engine.state;

public interface IState {

    enum Type {

        Initial("initial"),
        Connected("router range"),
        Disconnected("disconnected"),
        RemoteArea("remote area"),
        NearbyArea("nearby area"),
        CloseRange("close range");

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
