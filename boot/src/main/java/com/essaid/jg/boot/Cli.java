package com.essaid.jg.boot;

import com.essaid.jg.boot.cli.Main;
import com.essaid.jg.boot.cli.Start;
import com.essaid.jg.boot.grcp.GrpcConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;

@SpringBootApplication
@EnableConfigurationProperties
@Import(GrpcConfiguration.class)
public class Cli implements CommandLineRunner, ExitCodeGenerator {

    private final ConfigurableApplicationContext context;
    private final CommandLine.IFactory factory;
    private final Main main;
    private int exitCode;

    public Cli(ConfigurableApplicationContext context, CommandLine.IFactory factory, Main main) {
        this.context = context;
        this.factory = factory;
        this.main = main;
    }

    public static void main(String[] args) {
        SpringApplication cli = new SpringApplication(Cli.class);
        cli.run(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length == 0){
            return;
        }
        CommandLine cli = new CommandLine(this.context.getBean(Main.class));
        cli.addSubcommand("start", context.getBean(Start.class));
        cli.setExecutionStrategy(new CommandLine.RunAll());
        this.exitCode = cli.execute(args);
    }
}