package com.awesome.domains;

public interface AwesomeValidator<T> {
    boolean validate(T t);
    default boolean invalidate(T t){
        return !this.validate(t);
    }
}