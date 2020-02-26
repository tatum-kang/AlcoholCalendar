package com.rest.advice.exception;

public class CUserNotCorrectException extends RuntimeException {
	public CUserNotCorrectException(String msg, Throwable t) {
        super(msg, t);
    }
     
    public CUserNotCorrectException(String msg) {
        super(msg);
    }
     
    public CUserNotCorrectException() {
        super();
    }
}
