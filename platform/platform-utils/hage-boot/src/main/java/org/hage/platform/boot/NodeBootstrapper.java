package org.hage.platform.boot;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.springframework.context.ApplicationContext;


@Slf4j
public class NodeBootstrapper {

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

    public NodeBootstrapper(ApplicationContext applicationContext, String[] args) {
        log.info(HEADER);

        this.applicationContext = applicationContext;

        processCmdArgs(args);
    }


    public void start() {
        log.debug("Starting HAgE Node from the command line.");

        applicationContext.getBean(LifecycleEngine.class).start();

        log.debug("Bootstrapping finished.");
    }

    private void processCmdArgs(String[] args) {
        CommandLineArguments argumentsService = applicationContext.getBean(CommandLineArguments.class);

        argumentsService.parse(NodeBootstrapper.class, args);

        if (argumentsService.hasHelpOption()) {
            System.out.println(argumentsService.getUsage());
            System.exit(0);
        }

    }

}
