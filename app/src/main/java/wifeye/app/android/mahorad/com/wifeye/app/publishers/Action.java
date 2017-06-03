package wifeye.app.android.mahorad.com.wifeye.app.publishers;

public enum Action {

    Halt("Idle"),
    DisablingMode("Disabling"),
    ObserveModeDisabling("..disabling.."),
    ObserveModeEnabling("..enabling..");

    private final String title;

    Action(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
