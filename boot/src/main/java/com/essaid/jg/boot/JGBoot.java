package com.essaid.jg.boot;

import com.essaid.jg.boot.grcp.GrpcConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;

@SpringBootApplication
@EnableConfigurationProperties
@Import(GrpcConfiguration.class)
public class JGBoot implements CommandLineRunner, ExitCodeGenerator {

    private final CommandLine jgCli;
    private int exitCode;

    public JGBoot(CommandLine jgCli) {
        this.jgCli = jgCli;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(JGBoot.class);
        application.run(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            return;
        }
        this.exitCode = jgCli.execute(args);
    }
}