package com.commands.execution.engine.core.command;

import com.commands.execution.engine.core.command.contract.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ComponentScan("com.commands.execution.engine.core.command")
@PropertySource("classpath:command.properties")
public class CommandConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public CommandService commandService() {
        return new CommandServiceImpl(context);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {

        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages.core");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }

}
