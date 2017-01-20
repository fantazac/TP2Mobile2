package ca.csf.mobile2.tp2.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("TrapezoidFunction")
public abstract class TrapezoidFunctionJsonMixin extends TrapezoidFunction {

    @JsonCreator
    public TrapezoidFunctionJsonMixin(@JsonProperty("xStartMin") long xStartMin,
                                      @JsonProperty("xStartMax") long xStartMax,
                                      @JsonProperty("xEndMax") long xEndMax,
                                      @JsonProperty("xEndMin") long xEndMin,
                                      @JsonProperty("minValue") long minValue,
                                      @JsonProperty("maxValue") long maxValue) {
        super(xStartMin, xStartMax, xEndMax, xEndMin, minValue, maxValue);
    }

    @Override
    @JsonIgnore
    public abstract long getValue(long value);

    @Override
    @JsonProperty("xStartMin")
    public abstract long getXStartMin();

    @Override
    @JsonProperty("xStartMax")
    public abstract long getXStartMax();

    @Override
    @JsonProperty("xEndMax")
    public abstract long getXEndMax();

    @Override
    @JsonProperty("xEndMin")
    public abstract long getXEndMin();

    @Override
    @JsonProperty("minValue")
    public abstract long getMinValue();

    @Override
    @JsonProperty("maxValue")
    public abstract long getMaxValue();
}
