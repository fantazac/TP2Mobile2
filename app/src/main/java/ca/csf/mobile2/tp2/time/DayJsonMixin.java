package ca.csf.mobile2.tp2.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class DayJsonMixin extends Day {

    @JsonCreator
    public DayJsonMixin(@JsonProperty("timeAsString") String timeAsString) {
        super(timeAsString);
    }

    @Override
    @JsonProperty("timeAsString")
    public abstract String getTimeAsString();

    @Override
    @JsonIgnore
    public abstract long getTimeInSeconds();

    @Override
    @JsonIgnore
    public abstract boolean isOfTheSameDay(long utcTime);

}
