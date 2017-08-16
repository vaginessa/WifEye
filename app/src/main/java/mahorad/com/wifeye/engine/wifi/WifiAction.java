package mahorad.com.wifeye.engine.wifi;

public enum WifiAction {

        Halt("idle"),
        DisablingMode("disabling"),
        ObserveModeDisabling("..disabling.."),
        ObserveModeEnabling("..enabling..");

        private final String title;

        WifiAction(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }
