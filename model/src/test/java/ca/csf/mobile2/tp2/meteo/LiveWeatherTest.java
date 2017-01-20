package ca.csf.mobile2.tp2.meteo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.csf.mobile2.tp2.time.UtcTimeProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LiveWeatherTest {

    private WeatherForecastBundle weatherForecastBundle;
    private UtcTimeProvider utcTimeProvider;
    private LiveWeather liveWeather;

    @Before
    public void before() {
        utcTimeProvider = mock(UtcTimeProvider.class);
        weatherForecastBundle = mock(WeatherForecastBundle.class);

        liveWeather = new LiveWeather(weatherForecastBundle, utcTimeProvider);
    }

    @Test
    public void canGetTemperatureAccordingToCurrentTime() {
        long currentTime = 60L;
        int currentTemperature = 22;
        when(utcTimeProvider.getCurrentTimeInSeconds()).thenReturn(currentTime);
        when(weatherForecastBundle.getTemperatureAt(currentTime)).thenReturn(currentTemperature);

        assertEquals(currentTemperature, liveWeather.getCurrentTemperatureInCelsius());
    }

    @Test
    public void canGetCurrentTime() {
        long currentTime = 22L;
        when(utcTimeProvider.getCurrentTimeInSeconds()).thenReturn(currentTime);

        assertEquals(currentTime, liveWeather.getCurrentTime());
    }

    @Test
    public void canGetCurrentWeatherType() {
        long currentTime = 22L;
        WeatherType currentWeatherType = WeatherType.SNOW;
        when(utcTimeProvider.getCurrentTimeInSeconds()).thenReturn(currentTime);
        when(weatherForecastBundle.getWeatherTypeAt(currentTime)).thenReturn(currentWeatherType);

        assertEquals(currentWeatherType, liveWeather.getCurrentWeatherType());
    }

    @Test
    public void canStartLiveWeatherTimeStream() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);

        liveWeather.start(weatherListener);

        verify(utcTimeProvider).addTimeListener(any(UtcTimeProvider.TimeListener.class));
        verify(utcTimeProvider).start();
    }

    @Test
    public void startingAlreadyStartedLiveWeatherStreamDoesNothing() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);

        liveWeather.start(weatherListener);
        liveWeather.start(weatherListener);

        verify(utcTimeProvider, times(1)).addTimeListener(any(UtcTimeProvider.TimeListener.class));
        verify(utcTimeProvider, times(1)).start();
    }

    @Test
    public void canStopLiveWeatherTimeStream() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);
        liveWeather.start(weatherListener);
        ArgumentCaptor<UtcTimeProvider.TimeListener> timeListenerCaptor = ArgumentCaptor.forClass(UtcTimeProvider.TimeListener.class);
        verify(utcTimeProvider).addTimeListener(timeListenerCaptor.capture());

        liveWeather.stop();

        verify(utcTimeProvider).removeTimeListener(timeListenerCaptor.getValue());
        verify(utcTimeProvider).stop();
    }

    @Test
    public void stoppingStoppedLiveWeatherDoesNothing() {
        liveWeather.stop();
        liveWeather.stop();

        verify(utcTimeProvider, times(0)).addTimeListener(any(UtcTimeProvider.TimeListener.class));
        verify(utcTimeProvider, times(0)).removeTimeListener(any(UtcTimeProvider.TimeListener.class));
        verify(utcTimeProvider, times(0)).stop();
    }

    @Test
    public void notifiesWeatherListenersWhenTimeChanges() {
        LiveWeather.WeatherListener weatherListener = mock(LiveWeather.WeatherListener.class);
        ArgumentCaptor<UtcTimeProvider.TimeListener> timeListenerCaptor = ArgumentCaptor.forClass(UtcTimeProvider.TimeListener.class);

        liveWeather.start(weatherListener);
        verify(utcTimeProvider).addTimeListener(timeListenerCaptor.capture());
        timeListenerCaptor.getValue().onTimeChanged(utcTimeProvider);

        verify(weatherListener).onWeatherChanged(liveWeather);
    }

}