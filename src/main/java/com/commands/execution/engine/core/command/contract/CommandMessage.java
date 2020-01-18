package com.commands.execution.engine.core.command.contract;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommandMessage {

    @Builder.Default
    private final Type type = Type.INFO; 

    @Builder.Default
    private final Level level = Level.PARENT;

    private final String message;

    public static CommandMessage copyAsChild(final CommandMessage originalMessage) {
        return CommandMessage.builder()
                .message(originalMessage.getMessage())
                .type(originalMessage.getType())
                .level(Level.CHILD)
                .build();
    }

    public enum Type {
        INFO,
        WARNING,
        FAILURE
    }

    public enum Level {
        PARENT, CHILD
    }

    public boolean isFailure() {
        return type.equals(Type.FAILURE);
    }
}

