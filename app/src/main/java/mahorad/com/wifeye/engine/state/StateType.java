package mahorad.com.wifeye.engine.state;

public enum StateType {

        Initial("initial"),
        Connected("router range"),
        Disconnected("disconnected"),
        RemoteArea("remote area"),
        NearbyArea("nearby area"),
        CloseRange("close range");

        private final String title;

        StateType(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }
