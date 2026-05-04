package com.arthurscarpin.acs.core.user.exception;

public class EmailUserAlreadyExistsException extends RuntimeException {

    public EmailUserAlreadyExistsException(String message) {
        super(message);
    }
}
