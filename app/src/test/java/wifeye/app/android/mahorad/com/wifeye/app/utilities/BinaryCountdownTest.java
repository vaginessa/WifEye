package wifeye.app.android.mahorad.com.wifeye.app.utilities;

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
    public void singleRunTime_NoException_Completes() {
        BinaryCountdown timer = BinaryCountdown
                .builder()
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
    public void singleRunTime_HasException_NoCompletion() {
        BinaryCountdown timer = BinaryCountdown
                .builder()
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
    public void manyRunTime_NoException_Completes() {
        BinaryCountdown timer = BinaryCountdown
                .builder()
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