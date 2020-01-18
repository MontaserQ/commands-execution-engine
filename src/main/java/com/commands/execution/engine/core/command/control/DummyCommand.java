package com.commands.execution.engine.core.command.control;

import com.commands.execution.engine.core.command.contract.Command;
import com.commands.execution.engine.core.command.contract.CommandData;
import com.commands.execution.engine.core.command.contract.CommandResult;
import com.commands.execution.engine.core.command.data.ChildDummyCommandData;
import com.commands.execution.engine.core.command.data.DummyCommandData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
public class DummyCommand extends Command<DummyCommandData> {

    @Override
    public List<CommandData<? extends Command>> getCommandList(DummyCommandData data) {
        final List<CommandData<?>> commandDataList = new ArrayList<>();

        commandDataList.add(data);

        if (data.isExecuteChildCommand())
            commandDataList.add(new ChildDummyCommandData("Child", 47));

        return commandDataList;
    }

    @Override
    public CommandResult execute(DummyCommandData data) {
        return CommandResult.of("Dummy command executed with parameter [name = " + data.getName() + " ]");
    }
}
