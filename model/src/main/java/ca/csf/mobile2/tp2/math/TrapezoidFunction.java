package ca.csf.mobile2.tp2.math;

public class TrapezoidFunction implements MathFunction {

    private final long xStartMin;
    private final long xStartMax;
    private final long xEndMax;
    private final long xEndMin;
    private final long minValue;
    private final long maxValue;

    public TrapezoidFunction(long xStartMin, long xStartMax, long xEndMax, long xEndMin, long minValue, long maxValue) {
        this.xStartMin = xStartMin;
        this.xStartMax = xStartMax;
        this.xEndMax = xEndMax;
        this.xEndMin = xEndMin;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public long getValue(long value) {

        long variation = maxValue - minValue;

        if (value < xStartMin) {
            System.out.println(minValue);
            return minValue;
        }
        else if (value < xStartMax) {
            float interpolationFactor = (float)(value - xStartMin) / (float)(xStartMax - xStartMin);
            System.out.println(minValue + (int)(variation * interpolationFactor));
            return minValue + (int)(variation * interpolationFactor);
        }
        else if (value < xEndMax) {
            System.out.println(maxValue);
            return maxValue;
        }
        else if (value < xEndMin) {
            float interpolationFactor = (float)(xEndMin - value) / (float)(xEndMin - xEndMax);
            System.out.println(minValue + (int)(variation * interpolationFactor));
            return minValue + (int)(variation * interpolationFactor);
        }
        else {
            System.out.println(minValue);
            return minValue;
        }
    }

    public long getXStartMin() {
        return xStartMin;
    }

    public long getXStartMax() {
        return xStartMax;
    }

    public long getXEndMax() {
        return xEndMax;
    }

    public long getXEndMin() {
        return xEndMin;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

}
