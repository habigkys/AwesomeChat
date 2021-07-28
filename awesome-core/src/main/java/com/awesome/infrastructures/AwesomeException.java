package com.awesome.infrastructures;

public class AwesomeException extends RuntimeException{
    AwesomeException(){
        super();
    }
    public AwesomeException(String message){
        super(message);
    }
}
