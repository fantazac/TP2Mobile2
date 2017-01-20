package ca.csf.mobile2.tp2.meteo;

import org.junit.Before;
import org.junit.Test;

import ca.csf.mobile2.tp2.math.MathFunction;
import ca.csf.mobile2.tp2.time.UtcDay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherForecastTest {

    private static final long SOME_UTC_TIME = 1487635200L;
    private static final long SOME_OTHER_UTC_TIME = 1478874344L;
    private static final long SOME_TEMPERATURE = -35;
    private static final WeatherType SOME_WEATHER_TYPE = WeatherType.SUNNY;

    private UtcDay utcDay;
    private MathFunction mathFunction;
    private WeatherForecast weatherForecast;

    @Before
    public void before() {
        utcDay = mock(UtcDay.class);
        mathFunction = mock(MathFunction.class);
        weatherForecast = new WeatherForecast(utcDay, SOME_WEATHER_TYPE, mathFunction);
    }

    @Test
    public void canGetUtcDay() {
        assertEquals(utcDay, weatherForecast.getUtcDay());
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
        when(utcDay.isOfTheSameDay(SOME_UTC_TIME)).thenReturn(true);
        when(utcDay.isOfTheSameDay(SOME_OTHER_UTC_TIME)).thenReturn(false);

        assertTrue(weatherForecast.canGetTemperatureAt(SOME_UTC_TIME));
        assertFalse(weatherForecast.canGetTemperatureAt(SOME_OTHER_UTC_TIME));
    }

    @Test
    public void canGetTemperatureAccordingToMathFunction() {
        when(mathFunction.getValue(SOME_UTC_TIME)).thenReturn(SOME_TEMPERATURE);

        assertEquals(SOME_TEMPERATURE, weatherForecast.getTemperatureAt(SOME_UTC_TIME));
    }

}