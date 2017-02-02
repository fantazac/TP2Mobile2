package ca.csf.mobile2.tp2.meteo;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherForecastBundleTest {

    private static final String SOME_LOCATION = "Quebec, Canada";

    private WeatherForecast firstWeatherForecast;
    private List<WeatherForecast> weatherForecasts;
    private WeatherForecastBundle weatherForecastBundle;

    @Before
    public void before() {
        firstWeatherForecast = mock(WeatherForecast.class);
        weatherForecasts = new LinkedList<>();
        weatherForecasts.add(firstWeatherForecast);
        weatherForecastBundle = new WeatherForecastBundle(SOME_LOCATION, weatherForecasts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateWeatherBundleWithoutWeatherForecast() {
        weatherForecasts = new LinkedList<>();
        weatherForecastBundle = new WeatherForecastBundle(SOME_LOCATION, weatherForecasts);
    }

    @Test
    public void canGetLocation() {
        assertEquals(SOME_LOCATION, weatherForecastBundle.getLocationName());
    }

    @Test
    public void canGetWeatherForecasts() {
        for (int i = 0; i < weatherForecasts.size(); i++) {
            assertSame(weatherForecasts.get(i), weatherForecastBundle.get(i));
        }
    }

    @Test
    public void canIterateOverWeatherForecastBundle() {
        int i = 0;
        for (WeatherForecast weatherForecast : weatherForecasts) {
            assertSame(weatherForecasts.get(i), weatherForecast);
            i++;
        }
    }

    @Test
    public void canGetTemperatureForecastAccordingToTime() {
        when(firstWeatherForecast.canGetTemperatureAt(5)).thenReturn(true);
        when(firstWeatherForecast.getTemperatureAt(5)).thenReturn(35);

        WeatherForecast secondWeatherForecast = mock(WeatherForecast.class);
        when(secondWeatherForecast.canGetTemperatureAt(15)).thenReturn(true);
        when(secondWeatherForecast.getTemperatureAt(15)).thenReturn(22);
        weatherForecasts.add(secondWeatherForecast);

        WeatherForecast thirdWeatherForecast = mock(WeatherForecast.class);
        when(thirdWeatherForecast.canGetTemperatureAt(25)).thenReturn(true);
        when(thirdWeatherForecast.getTemperatureAt(25)).thenReturn(10);
        weatherForecasts.add(thirdWeatherForecast);

        assertEquals(35, weatherForecastBundle.getTemperatureAt(5));
        assertEquals(22, weatherForecastBundle.getTemperatureAt(15));
        assertEquals(10, weatherForecastBundle.getTemperatureAt(25));
    }

    @Test
    public void returnZeroWhenTimeIsOutOfWeatherForecastsRange() {
        when(firstWeatherForecast.canGetTemperatureAt(10)).thenReturn(false);

        WeatherForecast secondWeatherForecast = mock(WeatherForecast.class);
        when(secondWeatherForecast.canGetTemperatureAt(10)).thenReturn(false);
        weatherForecasts.add(secondWeatherForecast);

        WeatherForecast thirdWeatherForecast = mock(WeatherForecast.class);
        when(thirdWeatherForecast.canGetTemperatureAt(10)).thenReturn(false);
        weatherForecasts.add(thirdWeatherForecast);

        assertEquals(0, weatherForecastBundle.getTemperatureAt(15));
    }

    @Test
    public void canGetWeatherTypeAccordingToTime() {
        when(firstWeatherForecast.canGetTemperatureAt(5)).thenReturn(true);
        when(firstWeatherForecast.getWeather()).thenReturn(WeatherType.SNOW);

        WeatherForecast secondWeatherForecast = mock(WeatherForecast.class);
        when(secondWeatherForecast.canGetTemperatureAt(15)).thenReturn(true);
        when(secondWeatherForecast.getWeather()).thenReturn(WeatherType.SUNNY);
        weatherForecasts.add(secondWeatherForecast);

        WeatherForecast thirdWeatherForecast = mock(WeatherForecast.class);
        when(thirdWeatherForecast.canGetTemperatureAt(25)).thenReturn(true);
        when(thirdWeatherForecast.getWeather()).thenReturn(WeatherType.RAIN);
        weatherForecasts.add(thirdWeatherForecast);

        assertEquals(WeatherType.SNOW, weatherForecastBundle.getWeatherTypeAt(5));
        assertEquals(WeatherType.SUNNY, weatherForecastBundle.getWeatherTypeAt(15));
        assertEquals(WeatherType.RAIN, weatherForecastBundle.getWeatherTypeAt(25));
    }

    @Test
    public void returnNullWhenTimeIsOutOfWeatherForecastsRange() {
        when(firstWeatherForecast.canGetTemperatureAt(10)).thenReturn(false);

        WeatherForecast secondWeatherForecast = mock(WeatherForecast.class);
        when(secondWeatherForecast.canGetTemperatureAt(10)).thenReturn(false);
        weatherForecasts.add(secondWeatherForecast);

        WeatherForecast thirdWeatherForecast = mock(WeatherForecast.class);
        when(thirdWeatherForecast.canGetTemperatureAt(10)).thenReturn(false);
        weatherForecasts.add(thirdWeatherForecast);

        assertNull(weatherForecastBundle.getWeatherTypeAt(15));
    }

}