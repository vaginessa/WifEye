package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import org.junit.Test;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class UnaryCountdownTest {

    private int interval = 0;
    private int exception = 0;
    private int completed = 0;

    @Test
    public void singleRunTime_OneSecond_NoException_NoCompletion() {
        UnaryCountdown timer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .setRunTimes(1)
                .setIntervalsAction(() -> interval++)
                .build();

        timer.start();

        await().atMost(2, SECONDS)
               .until(() -> interval == 1);
        assertFalse(timer.isActive());
    }

    @Test
    public void singleRunTime_HasException_NoCompletion() {
        UnaryCountdown timer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .setRunTimes(1)
                .setIntervalsAction(() -> interval = interval/0)
                .setExceptionAction(() -> exception++)
                .build();

        timer.start();

        await()
                .atMost(2, SECONDS)
                .until(() -> exception == 1);
    }

    @Test
    public void singleRunTime_NoException_HasCompletion() {
        UnaryCountdown timer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .setRunTimes(1)
                .setIntervalsAction(() -> interval++)
                .setCompletionAction(() -> completed++)
                .build();

        timer.start();

        await()
                .atMost(2, SECONDS)
                .until(() -> interval == 1);

        assertEquals(1, completed);
        assertFalse(timer.isActive());
    }

    @Test
    public void manyRunTimes_NoException_NoCompletion() {
        UnaryCountdown timer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .setRunTimes(3)
                .setIntervalsAction(() -> interval++)
                .build();

        timer.start();

        await()
                .atMost(4, SECONDS)
                .until(() -> interval == 3);
    }

}