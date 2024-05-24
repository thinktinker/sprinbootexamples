package com.sctp.fsd.jpa.exceptions;

public class MessageNotReadableException extends RuntimeException {
    public MessageNotReadableException(){
        super("Data sent is not valid. Please check again.");
    }
}
