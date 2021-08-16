package com.awesome.domains;

public interface AwesomeValidator<T> {
    boolean validate(T t);
}