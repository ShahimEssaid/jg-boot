package com.essaid.jg.boot.cli;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@CommandLine.Command(name = "main",
        mixinStandardHelpOptions = true
        )
public class Main implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("Main ran ==================");
        return null;
    }
}
