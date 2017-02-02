package ca.csf.mobile2.tp2.meteo;

import ca.csf.mobile2.tp2.math.MathFunction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherForecastTest {

    private static final long SOME_TIME = 1487635200L;
    private static final long SOME_OTHER_TIME = 1478874344L;
    private static final long SOME_TEMPERATURE = -35;
    private static final WeatherType SOME_WEATHER_TYPE = WeatherType.SUNNY;

    private Day day;
    private MathFunction mathFunction;
    private WeatherForecast weatherForecast;

    @Before
    public void before() {
        day = mock(Day.class);
        mathFunction = mock(MathFunction.class);
        weatherForecast = new WeatherForecast(day, SOME_WEATHER_TYPE, mathFunction);
    }

    @Test
    public void canGetDay() {
        assertEquals(day, weatherForecast.getDay());
    }

    @Test
    public void canGetWeather() {
        assertEquals(SOME_WEATHER_TYPE, weatherForecast.getWeather());
    }

    @Test
    public void canGetMathFunctionForTemperature() {
        assertSame(mathFunction, weatherForecast.getTemperatureAccordingToUtcTimeFunction());
    }

    @Test
    public void canTellIfItCanGetTemperatureAtPreciseUtcTime() {
        when(day.isOfTheSameDay(SOME_TIME)).thenReturn(true);
        when(day.isOfTheSameDay(SOME_OTHER_TIME)).thenReturn(false);

        assertTrue(weatherForecast.canGetTemperatureAt(SOME_TIME));
        assertFalse(weatherForecast.canGetTemperatureAt(SOME_OTHER_TIME));
    }

    @Test
    public void canGetTemperatureAccordingToMathFunction() {
        when(mathFunction.getValue(SOME_TIME)).thenReturn(SOME_TEMPERATURE);

        assertEquals(SOME_TEMPERATURE, weatherForecast.getTemperatureAt(SOME_TIME));
    }

}