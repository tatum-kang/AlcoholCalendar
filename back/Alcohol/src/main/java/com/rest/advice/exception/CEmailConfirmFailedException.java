package com.rest.advice.exception;

public class CEmailConfirmFailedException extends RuntimeException {
	public CEmailConfirmFailedException(String msg, Throwable t) {
        super(msg, t);
    }
 
    public CEmailConfirmFailedException(String msg) {
        super(msg);
    }
 
    public CEmailConfirmFailedException() {
        super();
    }
}
