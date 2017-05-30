package wifeye.app.android.mahorad.com.wifeye.app.publishers;

public enum Action {

    Halt("idle"),
    DisablingMode("disabling"),
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
