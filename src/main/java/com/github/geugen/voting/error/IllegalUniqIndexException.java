package com.github.geugen.voting.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;


public class IllegalUniqIndexException extends AppException {
    public IllegalUniqIndexException(String msg) {
        super(HttpStatus.CONFLICT, msg, ErrorAttributeOptions.of(MESSAGE));
    }
}