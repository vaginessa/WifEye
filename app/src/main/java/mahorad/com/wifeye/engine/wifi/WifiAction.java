package mahorad.com.wifeye.engine.wifi;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

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
