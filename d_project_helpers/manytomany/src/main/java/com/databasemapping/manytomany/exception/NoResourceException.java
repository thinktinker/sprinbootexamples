package com.databasemapping.manytomany.exception;

public class NoResourceException extends RuntimeException {

    public NoResourceException() {
        super("Requested resource was not found.");
    }
}

