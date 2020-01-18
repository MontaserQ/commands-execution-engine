package com.commands.execution.engine.core.command.contract;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class CommandResult {

    private final List<CommandMessage> messages = new LinkedList<>();

    public static CommandResult of(final String message) {
        return new CommandResult().addMessage(message);
    }

    public static CommandResult of(final String message, final CommandMessage.Type type) {
        return new CommandResult().addMessage(message, type);
    }

    public CommandResult addMessage(final String message) {
        final CommandMessage commandMessage = CommandMessage.builder()
                .message(message)
                .build();
        this.messages.add(commandMessage);

        return this;
    }

    public CommandResult addMessage(final CommandMessage message) {
        if (message != null) {
            this.messages.add(message);
        }
        return this;
    }

    public CommandResult addMessages(final List<String> messages) {
        messages.forEach(this::addMessage);

        return this;
    }

    public CommandResult addMessage(final String message, final CommandMessage.Type type) {
        final CommandMessage commandMessage = CommandMessage.builder()
                .message(message)
                .type(type)
                .build();
        this.messages.add(commandMessage);

        return this;
    }

    public List<CommandMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public List<String> getMessagesPlain() {
        return messages.stream()
                .map(CommandMessage::getMessage)
                .collect(Collectors.toList());
    }
}

