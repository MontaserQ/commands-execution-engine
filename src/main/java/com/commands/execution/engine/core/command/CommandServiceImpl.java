package com.commands.execution.engine.core.command;

import com.commands.execution.engine.core.command.contract.Command;
import com.commands.execution.engine.core.command.contract.CommandData;
import com.commands.execution.engine.core.command.contract.CommandMessage;
import com.commands.execution.engine.core.command.contract.CommandService;
import com.commands.execution.engine.core.command.exception.CommandException;
import com.commands.execution.engine.core.command.exception.CommandServiceException;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CommandServiceImpl implements CommandService {

    private final ApplicationContext context;

    public CommandServiceImpl(final ApplicationContext context) {
        this.context = context;
    }

    @Override
    public List<CommandMessage> execute(final Collection<CommandData<?>> collection) throws CommandServiceException {
        final List<CommandMessage> result = new ArrayList<>();
        for (CommandData<?> commandData : collection)
            result.addAll(execute(commandData));
        return result;
    }

    @Override
    public List<CommandMessage> execute(final CommandData<?> commandData) throws CommandServiceException {
        final Class<? extends Command> commandClass = commandData.getCommandClass();
        final Command command = context.getBean(commandClass);

        final Class<?> commandDataClass = command.getCommandDataClass();

        if (!commandDataClass.equals(commandData.getClass())) {
            final String msg = String.format("Class mismatch '%s' vs '%s'", commandData.getClass(), commandDataClass);
            throw new IllegalStateException(msg);
        }

        try {
            command.validate(commandData);
            return executeCommandQueue(command, commandData);
        } catch (final CommandException e) {
            throw new CommandServiceException(e.getMessage());
        }

    }

    private List<CommandMessage> executeCommandQueue(final Command command, final CommandData commandData) throws CommandException, CommandServiceException {
        final List<CommandData> commandQueue = command.getCommandList(commandData);

        final List<CommandMessage> allMessages = new ArrayList<>();

        for (final CommandData queuedCommandData : commandQueue) {
            final List<CommandMessage> commandMessages = handleExecutionAsParentOrChild(commandData, queuedCommandData, command);
            allMessages.addAll(commandMessages);
        }
        return allMessages;
    }

    private List<CommandMessage> handleExecutionAsParentOrChild(final CommandData parentCommandData,
                                                                final CommandData commandData,
                                                                final Command command) throws CommandException, CommandServiceException {
        if (parentCommandData == commandData) {
            return executeParentCommand(commandData, command);
        } else {
            return executeChildCommandRecursively(commandData);
        }
    }

    private List<CommandMessage> executeParentCommand(final CommandData commandData, final Command command) throws CommandException {
        return command.execute(commandData).getMessages();
    }

    private List<CommandMessage> executeChildCommandRecursively(final CommandData commandData) throws CommandServiceException {
        final List<CommandMessage> commandMessages = execute(commandData);
        return commandMessages.stream().map(CommandMessage::copyAsChild).collect(Collectors.toList());
    }

}
