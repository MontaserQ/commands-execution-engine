package com.commands.execution.engine.core.command.exception;

public class CommandException extends Exception {

    public CommandException(final String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(final String message) {
        super(message);
    }
}
