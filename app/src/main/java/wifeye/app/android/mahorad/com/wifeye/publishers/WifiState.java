package wifeye.app.android.mahorad.com.wifeye.publishers;

public enum WifiState {
    Disabling(0),
    Disabled(1),
    Enabling(2),
    Enabled(3),
    Unknown(4);

    private int state;

    WifiState(int state) {
        this.state = state;
    }

    public static WifiState get(int state) {
        switch (state) {
            case 0: return Disabling;
            case 1: return Disabled;
            case 2: return Enabling;
            case 3: return Enabled;
            default: return Unknown;
        }
    }
}