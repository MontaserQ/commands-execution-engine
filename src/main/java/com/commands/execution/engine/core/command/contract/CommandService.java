package com.commands.execution.engine.core.command.contract;

import com.commands.execution.engine.core.command.exception.CommandServiceException;

import java.util.List;

public interface CommandService {

    List<CommandMessage> execute(final CommandData<?> commandData) throws CommandServiceException;
}
