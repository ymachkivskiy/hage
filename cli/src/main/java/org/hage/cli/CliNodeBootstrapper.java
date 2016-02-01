package org.hage.cli;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.PlatformConfiguration;
import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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

    private final CommandLineArgumentsService argumentsService;
    private final ApplicationContext applicationContext;

    public CliNodeBootstrapper(final String[] args) {
        System.out.print(HEADER);

        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class, PlatformConfiguration.class);

        argumentsService = applicationContext.getBean(CommandLineArgumentsService.class);
        argumentsService.parse(getClass().getName(), args);

    }

    public static void main(final String[] args) {
        new CliNodeBootstrapper(args).start();
    }

    public void start() {
        log.debug("Starting HAgE Node from the command line.");

        if(argumentsService.hasHelpOption()) {
            argumentsService.printUsage();
            return;
        }

        try {
            initialize();
        } catch(final InvalidRuntimeArgumentsException e) {
            log.error("Invalid runtime arguments.", e);
            argumentsService.printUsage();
        }
        log.debug("Bootstrapping finished.");
    }

    private void initialize() {
        applicationContext.getBean(LifecycleEngine.class).start();
    }
}
