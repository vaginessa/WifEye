package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface IOngoingActionConsumer {

    void onDisabling();

    void onObserveModeDisabling();

    void onObserveModeEnabling();

    void onHalted();
}
