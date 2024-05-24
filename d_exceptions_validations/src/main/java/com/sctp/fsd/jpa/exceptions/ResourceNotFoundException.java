package com.sctp.fsd.jpa.exceptions;

public class ResourceNotFoundException extends Throwable {

    public ResourceNotFoundException(String id) {
        super("Could not find Customer: " + id);
    }
}
