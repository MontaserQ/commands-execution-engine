package com.commands.execution.engine.core.command.data;

import com.commands.execution.engine.core.command.annotation.Required;
import com.commands.execution.engine.core.command.contract.CommandData;
import com.commands.execution.engine.core.command.control.ChildDummyCommand;
import com.commands.execution.engine.utility.MessageUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChildDummyCommandData implements CommandData<ChildDummyCommand> {

    @Required(message = MessageUtility.MISSING_ARGUMENT, messageArguments = "username")
    private String username;

    private Integer luckyNumber;
}
