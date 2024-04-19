package com.bigcompany.organization.exception;

public class FileParsingError extends RuntimeException{
    public FileParsingError(String message) {
        super(message);
    }
}
