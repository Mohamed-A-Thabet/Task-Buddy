package com.mohamed.management.exception;

public class NoUserFoundException  extends RuntimeException{

    public NoUserFoundException(String message) {
        super(message);
    }
}
