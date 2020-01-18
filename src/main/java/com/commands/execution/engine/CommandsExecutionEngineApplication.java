package com.commands.execution.engine;

import com.commands.execution.engine.core.command.contract.CommandMessage;
import com.commands.execution.engine.core.command.contract.CommandService;
import com.commands.execution.engine.core.command.data.DummyCommandData;
import com.commands.execution.engine.core.command.exception.CommandServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CommandsExecutionEngineApplication implements CommandLineRunner {


    @Autowired
    private CommandService commandService;

    public static void main(String[] args) {
        SpringApplication.run(CommandsExecutionEngineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final DummyCommandData dummyCommandData = new DummyCommandData("test",true);

        try {
            final List<CommandMessage> results = commandService.execute(dummyCommandData);
            for (CommandMessage message : results)
                System.out.println(message.getMessage());
        } catch (CommandServiceException e) {
            e.printStackTrace();
        }
    }
}
