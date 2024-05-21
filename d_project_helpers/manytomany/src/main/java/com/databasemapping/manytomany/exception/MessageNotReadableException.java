package com.databasemapping.manytomany.exception;

public class MessageNotReadableException extends RuntimeException {

    public MessageNotReadableException() {
        super("Unable to read request data.");
    }
}
