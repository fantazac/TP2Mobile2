package ca.csf.mobile2.tp2.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtcDayTest {

    private UtcDay utcDay;

    @Test
    public void truncatesHoursMinutesAndSecondsOfDay() {
        UtcDay utcDay = new UtcDay(1487559961L);
        assertEquals(1487548800L, utcDay.getUtcDayTime());

        utcDay = new UtcDay(1478874351L);
        assertEquals(1478822400L, utcDay.getUtcDayTime());

        utcDay = new UtcDay(1L);
        assertEquals(0L, utcDay.getUtcDayTime());
    }

    @Test
    public void canTellIfTimeIsOfTheSameDay() {
        UtcDay utcDay = new UtcDay(1433667001L);
        assertTrue(utcDay.isOfTheSameDay(1433690544L));
        assertFalse(utcDay.isOfTheSameDay(1476094510L));

        utcDay = new UtcDay(1476094210L);
        assertTrue(utcDay.isOfTheSameDay(1476094510L));
        assertFalse(utcDay.isOfTheSameDay(1433690544L));

        utcDay = new UtcDay(5000L);
        assertTrue(utcDay.isOfTheSameDay(15L));
        assertFalse(utcDay.isOfTheSameDay(86401L));
    }

}