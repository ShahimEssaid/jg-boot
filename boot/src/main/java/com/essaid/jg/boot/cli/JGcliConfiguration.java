package com.essaid.jg.boot.cli;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import picocli.CommandLine;

@Configuration
public class JGcliConfiguration {

    private final ConfigurableApplicationContext context;

    JGcliConfiguration(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Qualifier("jgCli")
    CommandLine jgCli() {
        CommandLine jgCli = new CommandLine(context.getBean(Cli.class));
        jgCli.addSubcommand(startCli());
        return jgCli;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    CommandLine startCli() {
        return new CommandLine(context.getBean(Start.class));
    }

}
