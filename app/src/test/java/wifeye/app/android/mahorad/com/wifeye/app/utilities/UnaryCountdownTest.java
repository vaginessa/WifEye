package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import org.junit.Test;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class UnaryCountdownTest {

    private UnaryCountdown timer;
    private int completed = 0;

    @Test
    public void elapse_NoCompletion_TimerElapses() {
        timer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .build();

        timer.start();

        await().atMost(2, SECONDS)
               .until(() -> timer.elapsed() == 1);
        assertFalse(timer.isActive());
    }

    @Test
    public void completion_CompletionActionRuns() {
        timer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();

        timer.start();

        await()
                .atMost(2, SECONDS)
                .until(() -> completed == 1);

        assertFalse(timer.isActive());
    }

    @Test
    public void stop_TimerStops() {
        timer = UnaryCountdown
                .builder()
                .setDuration(3, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();

        timer.start();
        await()
                .atMost(3, SECONDS)
                .until(() -> timer.elapsed() == 2);

        timer.stop();
        assertEquals(0, completed);
        assertFalse(timer.isActive());
    }

    @Test
    public void restart_AfterCompletion() {
        timer = UnaryCountdown
                .builder()
                .setDuration(2, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();

        timer.start();
        await()
                .atMost(3, SECONDS)
                .until(() -> !timer.isActive());

        assertEquals(2, timer.elapsed());
        timer.start();

        await()
                .atMost(3, SECONDS)
                .until(() -> !timer.isActive());

        assertEquals(2, completed);
        assertEquals(2, timer.elapsed());
        assertFalse(timer.isActive());
    }

    @Test
    public void restart_AfterStop() {
        timer = UnaryCountdown
                .builder()
                .setDuration(3, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();

        timer.start();
        await()
                .atMost(3, SECONDS)
                .until(() -> timer.elapsed() == 1);

        timer.stop();

        await()
                .atMost(2, SECONDS)
                .until(() -> !timer.isActive());

        timer.start();

        await()
                .atMost(4, SECONDS)
                .until(() -> !timer.isActive());

        assertEquals(1, completed);
        assertEquals(3, timer.elapsed());
        assertFalse(timer.isActive());
    }

}