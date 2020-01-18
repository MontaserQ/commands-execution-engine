package com.commands.execution.engine.core.command.exception;

public class CommandServiceException extends Exception{

    public CommandServiceException(final String message, Throwable cause) {
        super(message, cause);
    }

    public CommandServiceException(final String message) {
        super(message);
    }
}
