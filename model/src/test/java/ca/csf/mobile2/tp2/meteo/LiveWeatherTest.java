package ca.csf.mobile2.tp2.meteo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.csf.mobile2.tp2.time.TimeProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LiveWeatherTest {

    private WeatherForecastBundle weatherForecastBundle;
    private TimeProvider timeProvider;
    private LiveWeather liveWeather;

    @Before
    public void before() {
        timeProvider = mock(TimeProvider.class);
        weatherForecastBundle = mock(WeatherForecastBundle.class);

        liveWeather = new LiveWeather(weatherForecastBundle, timeProvider);
    }

    @Test
    public void canGetTemperatureAccordingToCurrentTime() {
        long currentTime = 60L;
        int currentTemperature = 22;
        when(timeProvider.getCurrentTimeInSeconds()).thenReturn(currentTime);
        when(weatherForecastBundle.getTemperatureAt(currentTime)).thenReturn(currentTemperature);

        assertEquals(currentTemperature, liveWeather.getCurrentTemperatureInCelsius());
    }

    @Test
    public void canGetCurrentTime() {
        long currentTime = 22L;
        when(timeProvider.getCurrentTimeInSeconds()).thenReturn(currentTime);

        assertEquals(currentTime, liveWeather.getCurrentTime());
    }

    @Test
    public void canGetCurrentWeatherType() {
        long currentTime = 22L;
        WeatherType currentWeatherType = WeatherType.SNOW;
        when(timeProvider.getCurrentTimeInSeconds()).thenReturn(currentTime);
        when(weatherForecastBundle.getWeatherTypeAt(currentTime)).thenReturn(currentWeatherType);

        assertEquals(currentWeatherType, liveWeather.getCurrentWeatherType());
    }

    @Test
    public void canStartLiveWeatherTimeStream() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);

        liveWeather.start(weatherListener);

        verify(timeProvider).addTimeListener(any(TimeProvider.TimeListener.class));
        verify(timeProvider).start();
    }

    @Test
    public void startingAlreadyStartedLiveWeatherStreamDoesNothing() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);

        liveWeather.start(weatherListener);
        liveWeather.start(weatherListener);

        verify(timeProvider, times(1)).addTimeListener(any(TimeProvider.TimeListener.class));
        verify(timeProvider, times(1)).start();
    }

    @Test
    public void canStopLiveWeatherTimeStream() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);
        liveWeather.start(weatherListener);
        ArgumentCaptor<TimeProvider.TimeListener> timeListenerCaptor = ArgumentCaptor.forClass(TimeProvider.TimeListener.class);
        verify(timeProvider).addTimeListener(timeListenerCaptor.capture());

        liveWeather.stop();

        verify(timeProvider).removeTimeListener(timeListenerCaptor.getValue());
        verify(timeProvider).stop();
    }

    @Test
    public void stoppingStoppedLiveWeatherDoesNothing() {
        liveWeather.stop();
        liveWeather.stop();

        verify(timeProvider, times(0)).addTimeListener(any(TimeProvider.TimeListener.class));
        verify(timeProvider, times(0)).removeTimeListener(any(TimeProvider.TimeListener.class));
        verify(timeProvider, times(0)).stop();
    }

    @Test
    public void notifiesWeatherListenersWhenTimeChanges() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);
        ArgumentCaptor<TimeProvider.TimeListener> timeListenerCaptor = ArgumentCaptor.forClass(TimeProvider.TimeListener.class);

        liveWeather.start(weatherListener);
        verify(timeProvider).addTimeListener(timeListenerCaptor.capture());
        timeListenerCaptor.getValue().onTimeChanged(timeProvider);

        verify(weatherListener).onWeatherChanged(liveWeather);
    }

}