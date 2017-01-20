package ca.csf.mobile2.tp2.math;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "functionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrapezoidFunction.class, name = "TrapezoidFunction")
})
public abstract class MathFunctionJsonMixin implements MathFunction {

}
