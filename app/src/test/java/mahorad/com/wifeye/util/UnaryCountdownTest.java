package mahorad.com.wifeye.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UnaryCountdownTest {

    private UnaryCountdown mainTimer;
    private int completed = 0;

    @Test
    public void elapse_NoCompletion_TimerElapses() {
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .build();
        mainTimer.subscribe(System.out::println);
        mainTimer.start();

        await().atMost(2, SECONDS)
               .until(() -> !mainTimer.isActive());
    }

    @Test
    public void completion_CompletionActionRuns() {
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(1, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();
        mainTimer.subscribe(System.out::println);
        mainTimer.start();

        await()
                .atMost(2, SECONDS)
                .until(() -> completed == 1);

        assertFalse(mainTimer.isActive());
    }

    @Test
    public void stop_TimerStops_noCompletion() {
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(3, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();
        Consumer<Long> consumer = (l -> {
            System.out.println(l);
            if (l == 1) mainTimer.stop();
        });
        mainTimer.subscribe(consumer);
        mainTimer.start();
        await()
                .atMost(3, SECONDS)
                .until(() -> !mainTimer.isActive());

        assertEquals(0, completed);
        assertFalse(mainTimer.isActive());
    }

    @Test
    public void restart_AfterCompletion() {
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(2, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();
        mainTimer.subscribe(System.out::println);
        mainTimer.start();

        await()
                .atMost(3, SECONDS)
                .until(() -> !mainTimer.isActive());

        mainTimer.start();

        await()
                .atMost(3, SECONDS)
                .until(() -> !mainTimer.isActive());

        assertEquals(2, completed);
        assertFalse(mainTimer.isActive());
    }

    @Test
    public void condition_StopsWhenConditionNotHolds() {
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(4, SECONDS)
                .setCompletionAction(() -> completed++)
                .setCondition(() -> completed == 0)
                .build();
        mainTimer.subscribe(System.out::println);

        UnaryCountdown modifier = UnaryCountdown
                .builder()
                .setDuration(2, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();
        modifier.subscribe(aLong -> System.out.println("-> " + aLong));

        mainTimer.start();
        modifier.start();

        await()
                .atMost(5, SECONDS)
                .until(() -> !mainTimer.isActive());

        assertEquals(2, completed);
        assertFalse(mainTimer.isActive());
    }

    @Test
    public void restart_AfterStop() {
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(3, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();
        Consumer<Long> consumer = (l -> {
            System.out.println(l);
            if (l == 1) mainTimer.stop();
        });
        mainTimer.subscribe(consumer);
        mainTimer.start();

        await()
                .atMost(3, SECONDS)
                .until(() -> !mainTimer.isActive());

        mainTimer.unsubscribe(consumer);
        mainTimer.subscribe(System.out::println);
        mainTimer.start();

        await()
                .atMost(4, SECONDS)
                .until(() -> !mainTimer.isActive());

        assertEquals(1, completed);
        assertFalse(mainTimer.isActive());
    }


    @Test
    public void resubscribe_ReceivesProperTicks() {
        int duration = 6;
        int unsub = 2;
        int resub = 5;

        final List<Long> longList = new ArrayList<>();
        final Consumer<Long> consumer = (longList::add);
        mainTimer = UnaryCountdown
                .builder()
                .setDuration(duration, SECONDS)
                .setCompletionAction(() -> completed++)
                .build();
        mainTimer.subscribe(consumer);

        UnaryCountdown unsubTimer = UnaryCountdown
                .builder()
                .setDuration(unsub, SECONDS)
                .setCompletionAction(() -> mainTimer.unsubscribe(consumer))
                .build();

        UnaryCountdown resubTimer = UnaryCountdown
                .builder()
                .setDuration(resub, SECONDS)
                .setCompletionAction(() -> mainTimer.subscribe(consumer))
                .build();

        mainTimer.start();
        unsubTimer.start();
        resubTimer.start();

        await()
                .atMost(duration + 1, SECONDS)
                .until(() -> !mainTimer.isActive());

        assertTrue(longList.contains((long)0));
        assertTrue(longList.contains((long)1));
        assertFalse(longList.contains((long) 2));
        assertFalse(longList.contains((long) 3));
        assertFalse(longList.contains((long) 4));
        assertTrue(longList.contains((long)5));
    }

}