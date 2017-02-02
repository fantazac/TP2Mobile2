package ca.csf.mobile2.tp2.time;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TimedUtcTimeProviderTest {

    private static final long SOME_INTERVAL_TIME = 1000L;

    private Handler handler;
    private TimedUtcTimeProvider timedUtcTimeProvider;

    @Before
    public void before() {
        handler = mock(Handler.class);
        timedUtcTimeProvider = new TimedUtcTimeProvider(handler, SOME_INTERVAL_TIME);
    }

    @Test
    public void canGetCurrentTimeInSeconds() {
        assertEquals(Calendar.getInstance().getTimeInMillis() / 1000, timedUtcTimeProvider.getCurrentTimeInSeconds());
    }

    @Test
    public void canStartTimeProvider() {
        timedUtcTimeProvider.start();

        verify(handler).postDelayed(any(Runnable.class), eq(SOME_INTERVAL_TIME));
    }

    @Test
    public void cannotStartTimeProviderTwice() {
        timedUtcTimeProvider.start();
        timedUtcTimeProvider.start();

        verify(handler, times(1)).postDelayed(any(Runnable.class), eq(SOME_INTERVAL_TIME));
    }

    @Test
    public void canStopStartedTimeProvider() {
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        timedUtcTimeProvider.start();
        timedUtcTimeProvider.stop();

        verify(handler).postDelayed(runnableCaptor.capture(), eq(SOME_INTERVAL_TIME));
        verify(handler).removeCallbacks(runnableCaptor.getValue());
    }

    @Test
    public void cannotStopUnStaredTimeProvider() {
        timedUtcTimeProvider.stop();

        verify(handler, times(0)).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void cannotStopTimeProviderTwice() {
        timedUtcTimeProvider.start();
        timedUtcTimeProvider.stop();
        timedUtcTimeProvider.stop();

        verify(handler, times(1)).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void canAddTimeListener() {
        TimeProvider.TimeListener timeListener = mock(TimeProvider.TimeListener.class);

        timedUtcTimeProvider.addTimeListener(timeListener);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        timedUtcTimeProvider.start();
        verify(handler).postDelayed(runnableCaptor.capture(), eq(SOME_INTERVAL_TIME));
        runnableCaptor.getValue().run();
        verify(timeListener).onTimeChanged(timedUtcTimeProvider);
    }

    @Test
    public void canRemoveTimeListener() {
        TimeProvider.TimeListener timeListener1 = mock(TimeProvider.TimeListener.class);
        TimeProvider.TimeListener timeListener2 = mock(TimeProvider.TimeListener.class);
        timedUtcTimeProvider.addTimeListener(timeListener1);
        timedUtcTimeProvider.addTimeListener(timeListener2);

        timedUtcTimeProvider.removeTimeListener(timeListener2);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        timedUtcTimeProvider.start();
        verify(handler).postDelayed(runnableCaptor.capture(), eq(SOME_INTERVAL_TIME));
        runnableCaptor.getValue().run();
        verify(timeListener1).onTimeChanged(timedUtcTimeProvider);
        verify(timeListener2, times(0)).onTimeChanged(timedUtcTimeProvider);
    }

    @Test
    public void notifiesAllTimeListenersAtTimeInterval() {
        TimeProvider.TimeListener timeListener1 = mock(TimeProvider.TimeListener.class);
        TimeProvider.TimeListener timeListener2 = mock(TimeProvider.TimeListener.class);
        timedUtcTimeProvider.addTimeListener(timeListener1);
        timedUtcTimeProvider.addTimeListener(timeListener2);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        timedUtcTimeProvider.start();

        //Check that the Handler was scheduled once
        verify(handler).postDelayed(runnableCaptor.capture(), eq(SOME_INTERVAL_TIME));
        runnableCaptor.getValue().run();

        verify(timeListener1).onTimeChanged(timedUtcTimeProvider);
        verify(timeListener2).onTimeChanged(timedUtcTimeProvider);
        //Check that the Handler is rescheduled
        verify(handler, times(2)).postDelayed(same(runnableCaptor.getValue()), eq(SOME_INTERVAL_TIME));
        runnableCaptor.getValue().run();
        verify(timeListener1, times(2)).onTimeChanged(timedUtcTimeProvider);
        verify(timeListener2, times(2)).onTimeChanged(timedUtcTimeProvider);
    }

}