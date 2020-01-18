package com.commands.execution.engine.core.command.data;

import com.commands.execution.engine.core.command.control.DummyCommand;
import com.commands.execution.engine.utility.MessageUtility;
import com.commands.execution.engine.core.command.annotation.Required;
import com.commands.execution.engine.core.command.contract.CommandData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DummyCommandData implements CommandData<DummyCommand> {

    @Required(messageArguments = "name", message = MessageUtility.MISSING_ARGUMENT)
    private String name;
    private boolean executeChildCommand;
}
