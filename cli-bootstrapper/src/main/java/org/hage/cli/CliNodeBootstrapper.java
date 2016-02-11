package org.hage.cli;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.PlatformConfiguration;
import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.hage.util.cmd.CommandLineArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
public class CliNodeBootstrapper {

    private static final String HEADER =
        "+-------------------------------+\n"
            + "|Hh  hH   AAA           EEEEEEE |\n"
            + "|Hh  hH  AAAAA   gggggg EE      |\n"
            + "|hhhhhH AA   AA gg   gg EEEEE   |\n"
            + "|hhhhhH AAAAAAA ggggggg EE      |\n"
            + "|Hh  hH AA   AA      gg EEEEEEE |\n"
            + "|Hh  hH          ggggg          |\n"
            + "+-------------------------------+\n\n";

    private final ApplicationContext applicationContext;

    public CliNodeBootstrapper(final String[] args) {
        System.out.print(HEADER);

        applicationContext = new AnnotationConfigApplicationContext(Config.class, PlatformConfiguration.class);

        processCmdArgs(args);
    }


    public void start() {
        log.debug("Starting HAgE Node from the command line.");

        applicationContext.getBean(LifecycleEngine.class).start();

        log.debug("Bootstrapping finished.");
    }

    private void processCmdArgs(String[] args) {
        CommandLineArguments argumentsService = applicationContext.getBean(CommandLineArguments.class);

        argumentsService.parse(CliNodeBootstrapper.class, args);

        if (argumentsService.hasHelpOption()) {
            System.out.println(argumentsService.getUsage());
            System.exit(0);
        }

    }

    @Configuration
    public static class Config {
        @Bean
        public CommandLineArguments commandLineArgumentsService() {
            return new CommandLineArguments();
        }
    }

    public static void main(final String[] args) {
        new CliNodeBootstrapper(args).start();
    }

}
