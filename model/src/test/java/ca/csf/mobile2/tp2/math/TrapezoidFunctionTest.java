package ca.csf.mobile2.tp2.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrapezoidFunctionTest {

    private static final long X_START_MIN = 2;
    private static final long X_START_MAX = 4;
    private static final long X_END_MAX = 6;
    private static final long X_END_MIN = 8;
    private static final long MIN_VALUE = 4;
    private static final long MAX_VALUE = 10;

    private TrapezoidFunction trapezoidFunction;

    @Before
    public void before() {
        trapezoidFunction = new TrapezoidFunction(X_START_MIN, X_START_MAX, X_END_MAX, X_END_MIN, MIN_VALUE, MAX_VALUE);
    }

    @Test
    public void canGetXStartMin() {
        assertEquals(X_START_MIN, trapezoidFunction.getXStartMin());
    }

    @Test
    public void canGetXStartMax() {
        assertEquals(X_START_MAX, trapezoidFunction.getXStartMax());
    }

    @Test
    public void canGetXEndMax() {
        assertEquals(X_END_MAX, trapezoidFunction.getXEndMax());
    }

    @Test
    public void canGetXEndMin() {
        assertEquals(X_END_MIN, trapezoidFunction.getXEndMin());
    }

    @Test
    public void canGetMinValue() {
        assertEquals(MIN_VALUE, trapezoidFunction.getMinValue());
    }

    @Test
    public void canGetMaxValue() {
        assertEquals(MAX_VALUE, trapezoidFunction.getMaxValue());
    }

    @Test
    public void canComputeLeftLowerPartOfTrapezoidFunction() {
        trapezoidFunction = new TrapezoidFunction(2, 4, 6, 8, 5, 10);

        assertEquals(5, trapezoidFunction.getValue(0));
        assertEquals(5, trapezoidFunction.getValue(1));
        assertEquals(5, trapezoidFunction.getValue(2));
    }

    @Test
    public void canComputeRightLowerPartOfTrapezoidFunction() {
        trapezoidFunction = new TrapezoidFunction(2, 4, 6, 8, 5, 10);

        assertEquals(5, trapezoidFunction.getValue(8));
        assertEquals(5, trapezoidFunction.getValue(9));
        assertEquals(5, trapezoidFunction.getValue(150));
    }

    @Test
    public void canComputeLeftRaisingPartOfTrapezoidFunction() {
        trapezoidFunction = new TrapezoidFunction(2, 4, 6, 8, 5, 10);

        assertEquals(5, trapezoidFunction.getValue(2));
        assertEquals(7, trapezoidFunction.getValue(3));
        assertEquals(10, trapezoidFunction.getValue(4));
    }

    @Test
    public void canComputeRightLoweringPartOfTrapezoidFunction() {
        trapezoidFunction = new TrapezoidFunction(2, 4, 6, 8, 5, 10);

        assertEquals(10, trapezoidFunction.getValue(6));
        assertEquals(7, trapezoidFunction.getValue(7));
        assertEquals(5, trapezoidFunction.getValue(8));
    }

    @Test
    public void canComputeTopPartOfTrapezoidFunction() {
        trapezoidFunction = new TrapezoidFunction(2, 4, 6, 8, 5, 10);

        assertEquals(10, trapezoidFunction.getValue(4));
        assertEquals(10, trapezoidFunction.getValue(5));
        assertEquals(10, trapezoidFunction.getValue(6));
    }

}