package ca.csf.mobile2.tp2.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UtcDayJsonMixin extends UtcDay {

    @JsonCreator
    public UtcDayJsonMixin(@JsonProperty("utcDayTime") long utcTime) {
        super(utcTime);
    }

    @Override
    @JsonProperty("utcDayTime")
    public abstract long getUtcDayTime();

    @Override
    @JsonIgnore
    public abstract boolean isOfTheSameDay(long utcTime);
}
