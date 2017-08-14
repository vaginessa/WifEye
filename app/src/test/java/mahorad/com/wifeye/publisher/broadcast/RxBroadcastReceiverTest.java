package mahorad.com.wifeye.publisher.broadcast;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import io.reactivex.observers.TestObserver;
import mahorad.com.wifeye.publisher.broadcast.RxBroadcastReceiver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class RxBroadcastReceiverTest {

    @Test
    public void createWithNullThrows() {
        try {
            //noinspection ResourceType
            RxBroadcastReceiver.create(null, null);
            fail();
        } catch (NullPointerException e) {
            assertThat(e).hasMessage("context == null");
        }
    }

    @Test
    public void subscribe_ReceivesTargetIntentsBeforeCancel() {
        IntentFilter intentFilter = new IntentFilter("test_action");
        Application application = RuntimeEnvironment.application;

        TestObserver<Intent> observer = RxBroadcastReceiver.create(application, intentFilter).test();

        observer.assertValues();

        Intent intent1 = new Intent("test_action").putExtra("foo", "bar");
        application.sendBroadcast(intent1);
        observer.assertValues(intent1);

        Intent intent2 = new Intent("test_action").putExtra("bar", "baz");
        application.sendBroadcast(intent2);
        observer.assertValues(intent1, intent2);

        Intent intent3 = new Intent("test_action_ignored");
        application.sendBroadcast(intent3);
        observer.assertValues(intent1, intent2);

        Intent intent4 = new Intent("test_action").putExtra("bar", "baz");
        observer.cancel();
        application.sendBroadcast(intent4);
        observer.assertValues(intent1, intent2);
    }
}