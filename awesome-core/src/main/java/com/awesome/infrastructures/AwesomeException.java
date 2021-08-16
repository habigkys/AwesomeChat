package com.awesome.infrastructures;

public class AwesomeException extends RuntimeException {
    AwesomeExceptionType exceptionType;

    AwesomeException(){
        super();
    }

    public AwesomeException(String message){
        super(message);
    }

    public AwesomeException(AwesomeExceptionType awesomeExceptionType) {
        super(awesomeExceptionType.name());
        this.exceptionType = awesomeExceptionType;
    }
}
