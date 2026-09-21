// src/main/java/com/gitStatus/gitStatus/exception/ResourceNotFoundException.java
package com.gitStatus.gitStatus.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}