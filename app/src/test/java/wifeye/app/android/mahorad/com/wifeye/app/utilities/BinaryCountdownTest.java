package wifeye.app.android.mahorad.com.wifeye.app.utilities;

import org.junit.Test;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BinaryCountdownTest {

    private BinaryCountdown timer;

    int lessDelayedNumber = 0;
    int moreDelayedNumber = 0;
    int exceptionNumber = 0;
    boolean isCompleted = false;

    @Test
    public void singleRunTime_NoException_Completes() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(1)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithMoreDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(4, SECONDS)
               .until(() -> !timer.isActive());

        assertEquals(1, lessDelayedNumber);
        assertEquals(1, moreDelayedNumber);
        assertTrue(isCompleted);
        assertFalse(timer.isActive());
    }

    @Test
    public void exception_singleRunTime_NoCompletion() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(1)
                .setLessDelayedLength(2, SECONDS)
                .setMoreDelayedLength(4, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedAction(() -> moreDelayedNumber = moreDelayedNumber / 0)
                .setCompletionAction(() -> isCompleted = true)
                .setExceptionAction(() -> exceptionNumber++)
                .startWithMoreDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(10, SECONDS)
               .until(() -> !timer.isActive());

        assertEquals(1, lessDelayedNumber);
        assertTrue(isCompleted);
    }

    @Test
    public void manyRunTime_NoException_Completes() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(2)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithMoreDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(20, SECONDS)
                .until(() -> isCompleted);

        assertEquals(2, lessDelayedNumber);
        assertEquals(2, moreDelayedNumber);
        assertFalse(timer.isActive());
    }

    @Test
    public void stop_FromInside_TimerStops() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(2)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> timer.stop())
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithMoreDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(4, SECONDS)
                .until(() -> !timer.isActive());

        assertEquals(1, moreDelayedNumber);
        assertEquals(0, lessDelayedNumber);
    }

    @Test
    public void stop_FromOutside_TimerStops() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(3)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithMoreDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(6, SECONDS)
                .until(() -> moreDelayedNumber == 2);

        timer.stop();

        assertEquals(1, lessDelayedNumber);
        assertFalse(isCompleted);
        assertFalse(timer.isActive());
    }

    @Test
    public void restart_AfterCompletion() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(1)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithMoreDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(4, SECONDS)
                .until(() -> !timer.isActive());

        assertTrue(isCompleted);
        assertEquals(1, lessDelayedNumber);
        assertEquals(1, moreDelayedNumber);

        isCompleted = false;
        timer.start();

        await().atMost(4, SECONDS)
                .until(() -> !timer.isActive());

        assertTrue(isCompleted);
        assertEquals(2, lessDelayedNumber);
        assertEquals(2, moreDelayedNumber);
    }

    @Test
    public void restart_AfterStop() {
        timer = BinaryCountdown
                .builder()
                .setRunTimes(1)
                .setLessDelayedLength(1, SECONDS)
                .setLessDelayedAction(() -> lessDelayedNumber++)
                .setMoreDelayedLength(2, SECONDS)
                .setMoreDelayedAction(() -> moreDelayedNumber++)
                .setCompletionAction(() -> isCompleted = true)
                .startWithLessDelayedAction()
                .build();
        timer.subscribe(System.out::println);
        timer.start();

        await().atMost(2, SECONDS)
                .until(() -> lessDelayedNumber == 1);

        timer.stop();

        await().atMost(1, SECONDS)
                .until(() -> !timer.isActive());

        assertFalse(isCompleted);
        assertEquals(0, moreDelayedNumber);

        timer.start();

        await().atMost(4, SECONDS)
                .until(() -> !timer.isActive());

        assertTrue(isCompleted);
        assertEquals(2, lessDelayedNumber);
        assertEquals(1, moreDelayedNumber);
    }


}