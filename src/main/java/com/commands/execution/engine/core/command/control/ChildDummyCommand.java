package com.commands.execution.engine.core.command.control;

import com.commands.execution.engine.core.command.contract.Command;
import com.commands.execution.engine.core.command.contract.CommandResult;
import com.commands.execution.engine.core.command.data.ChildDummyCommandData;
import com.commands.execution.engine.core.command.exception.CommandException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class ChildDummyCommand extends Command<ChildDummyCommandData> {


    @Override
    public CommandResult execute(final ChildDummyCommandData data) throws CommandException {

        return CommandResult.of("Child Dummy Command has been executed with parameters [username = " + data.getUsername() + " , luckyNumber = " + data.getLuckyNumber() + " ]");
    }
}
