package com.awesome.domains;

public interface AwesomeBiValidator<T1,T2> {
    boolean validate(T1 t1, T2 t2);
}