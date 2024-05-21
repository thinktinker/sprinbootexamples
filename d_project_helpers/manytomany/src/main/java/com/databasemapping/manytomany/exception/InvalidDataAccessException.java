package com.databasemapping.manytomany.exception;

public class InvalidDataAccessException extends RuntimeException {

    public InvalidDataAccessException(String message) {
        super(message);
    }
}
