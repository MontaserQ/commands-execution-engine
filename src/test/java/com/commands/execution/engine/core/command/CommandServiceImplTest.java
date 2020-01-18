package com.commands.execution.engine.core.command;

import com.commands.execution.engine.core.command.annotation.Required;
import com.commands.execution.engine.core.command.contract.Command;
import com.commands.execution.engine.core.command.contract.CommandData;
import com.commands.execution.engine.core.command.contract.CommandMessage;
import com.commands.execution.engine.core.command.contract.CommandResult;
import com.commands.execution.engine.core.command.exception.CommandException;
import com.commands.execution.engine.core.command.exception.CommandServiceException;
import com.commands.execution.engine.utility.MessageUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
class CommandServiceImplTest {

    @Mock
    private ApplicationContext context;

    @InjectMocks
    private CommandServiceImpl underTest;

    @Test
    public void testExecuteSuccessful() throws CommandServiceException {
        when(context.getBean(CommandB.class)).thenReturn(new CommandB());
        when(context.getBean(CommandA.class)).thenReturn(new CommandA());

        final CommandAData data = new CommandAData("Parent command", 4);
        final List<CommandMessage> result = underTest.execute(data);
        assertThat(result.get(0).getMessage()).isEqualTo("Execute command A");
        assertThat(result.get(0).getType()).isEqualTo(CommandMessage.Type.INFO);
        assertThat(result.get(0).getLevel()).isEqualTo(CommandMessage.Level.PARENT);

        assertThat(result.get(1).getMessage()).isEqualTo("Execute command B");
        assertThat(result.get(1).getType()).isEqualTo(CommandMessage.Type.INFO);
        assertThat(result.get(1).getLevel()).isEqualTo(CommandMessage.Level.CHILD);
    }


    @Test
    public void testExecuteCollectionOfCommandsSuccessfully() throws CommandServiceException {
        when(context.getBean(CommandB.class)).thenReturn(new CommandB());
        when(context.getBean(CommandA.class)).thenReturn(new CommandA());

        Collection<CommandData<?>> commands = new ArrayList<>();
        final CommandAData data = new CommandAData("Parent command", 4);

        commands.add(data);
        commands.add(data);

        final List<CommandMessage> result = underTest.execute(commands);

        for (int i = 0; i < 4; i += 2) {
            assertThat(result.get(i).getMessage()).isEqualTo("Execute command A");
            assertThat(result.get(i).getType()).isEqualTo(CommandMessage.Type.INFO);
            assertThat(result.get(i).getLevel()).isEqualTo(CommandMessage.Level.PARENT);

            assertThat(result.get(i + 1).getMessage()).isEqualTo("Execute command B");
            assertThat(result.get(i + 1).getType()).isEqualTo(CommandMessage.Type.INFO);
            assertThat(result.get(i + 1).getLevel()).isEqualTo(CommandMessage.Level.CHILD);
        }
    }

    @Test
    public void testCommandClassMismatch() {
        when(context.getBean(CommandB.class)).thenReturn(new CommandB());

        final MismatchedCommand data = new MismatchedCommand("should failed");
        assertThatThrownBy(() -> underTest.execute(data))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Class mismatch 'class com.commands.execution.engine.core.command.CommandServiceImplTest$MismatchedCommand' vs 'class com.commands.execution.engine.core.command.CommandServiceImplTest$CommandBData'");
    }

    @Test
    public void testCommandValidationError() {
        final StrictCommandData data = new StrictCommandData();
        when(context.getBean(StricCommand.class)).thenReturn(new StricCommand());

        assertThatThrownBy(() -> underTest.execute(data))
                .isInstanceOf(CommandServiceException.class)
                .hasMessage("\"requiredField\" must be provided.");
    }


    private class CommandA extends Command<CommandAData> {

        @Override
        public List<CommandData<? extends Command>> getCommandList(CommandAData data) {
            List<CommandData<?>> commandDataList = new ArrayList<>();

            commandDataList.add(data);

            if (data.luckyNumber == 4 || data.luckyNumber == 7)
                commandDataList.add(new CommandBData("random string from child command"));

            return commandDataList;
        }

        @Override
        public CommandResult execute(CommandAData data) throws CommandException {
            return CommandResult.of("Execute command A");
        }
    }

    private class CommandB extends Command<CommandBData> {

        @Override
        public CommandResult execute(CommandBData data) throws CommandException {
            return CommandResult.of("Execute command B");
        }
    }

    public class CommandAData implements CommandData<CommandA> {

        private String name;

        private Integer luckyNumber;

        public CommandAData(String name, Integer luckyNumber) {
            this.name = name;
            this.luckyNumber = luckyNumber;
        }
    }


    public class CommandBData implements CommandData<CommandB> {

        private String statement;

        public CommandBData(String statement) {
            this.statement = statement;
        }
    }


    public class MismatchedCommand implements CommandData<CommandB> {

        private String statement;

        public MismatchedCommand(String statement) {
            this.statement = statement;
        }
    }

    public class StricCommand extends Command<StrictCommandData> {

        @Override
        public CommandResult execute(StrictCommandData data) throws CommandException {
            return null;
        }
    }

    public class StrictCommandData implements CommandData<StricCommand> {

        @Required(message = MessageUtility.MISSING_ARGUMENT, messageArguments = "requiredField")
        private String requiredField;
    }

}