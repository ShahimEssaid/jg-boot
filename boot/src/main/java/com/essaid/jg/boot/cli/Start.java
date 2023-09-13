package com.essaid.jg.boot.cli;

import com.essaid.jg.boot.JGServer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@CommandLine.Command(name = "start")
public class Start implements Runnable {

    private final JGServer server;

    public Start(JGServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.start().join();
    }
}
