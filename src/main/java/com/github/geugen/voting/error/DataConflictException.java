package com.github.geugen.voting.error;


public class DataConflictException extends RuntimeException {

    public DataConflictException(String msg) {
        super(msg);
    }
}