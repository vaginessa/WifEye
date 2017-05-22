package wifeye.app.android.mahorad.com.wifeye.utilities;

import org.junit.Test;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BinaryCountdownTest {

    int lessDelayedNumber = 0;
    int moreDelayedNumber = 0;
    int exceptionNumber = 0;
    boolean isCompleted = false;

    @Test
    public void singleEnact_NoException_Completes() {
        BinaryCountdown timer = new BinaryCountdownBuilder()
                .setRunTimes(1)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithMoreDelayedAction()
                .build();
        timer.start();

        await().atMost(3, SECONDS)
               .until(() -> isCompleted);

        assertEquals(1, lessDelayedNumber);
        assertEquals(1, moreDelayedNumber);
        assertFalse(timer.isActive());
    }

    @Test
    public void singleEnact_HasException_NoCompletion() {
        BinaryCountdown timer = new BinaryCountdownBuilder()
                .setRunTimes(1)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber = moreDelayedNumber / 0)
                .setCompletionAction(() -> isCompleted = true)
                .setExceptionAction(() -> exceptionNumber++)
                .startWithMoreDelayedAction()
                .build();
        timer.start();

        await().atMost(3, SECONDS)
               .until(() -> exceptionNumber == 1);

        assertEquals(0, lessDelayedNumber);
        assertEquals(0, moreDelayedNumber);
        assertFalse(timer.isActive());
    }

    @Test
    public void manyEnacts_NoException_Completes() {
        BinaryCountdown timer = new BinaryCountdownBuilder()
                .setRunTimes(2)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .setExceptionAction(() -> exceptionNumber++)
                .startWithMoreDelayedAction()
                .build();
        timer.start();

        await().atMost(7, SECONDS)
                .until(() -> isCompleted);

        assertEquals(2, lessDelayedNumber);
        assertEquals(2, moreDelayedNumber);
        assertFalse(timer.isActive());
    }


}