package org.hage.cli;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.argument.InvalidRuntimeArgumentsException;
import org.hage.platform.argument.RuntimeArgumentsService;
import org.hage.services.core.LifecycleManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@Slf4j
public class CliNodeBootstrapper {

    private static final String APP_CONTEXT = "spring/appconfig.xml";

    private static final String HEADER =
            "+-------------------------------+\n"
                    + "|Hh  hH   AAA           EEEEEEE |\n"
                    + "|Hh  hH  AAAAA   gggggg EE      |\n"
                    + "|hhhhhH AA   AA gg   gg EEEEE   |\n"
                    + "|hhhhhH AAAAAAA ggggggg EE      |\n"
                    + "|Hh  hH AA   AA      gg EEEEEEE |\n"
                    + "|Hh  hH          ggggg          |\n"
                    + "+-------------------------------+\n\n";

    private final RuntimeArgumentsService argumentsService;
    private final ClassPathXmlApplicationContext applicationContext;

    public CliNodeBootstrapper(final String[] args) {
        System.out.print(HEADER);

        applicationContext = new ClassPathXmlApplicationContext(APP_CONTEXT);

        argumentsService = applicationContext.getBean(RuntimeArgumentsService.class);
        argumentsService.parse(getClass().getName(), args);

    }

    public static void main(final String[] args) {
        new CliNodeBootstrapper(args).start();
    }

    public void start() {
        log.debug("Starting AgE Node from the command line.");

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
        applicationContext.getBean(LifecycleManager.class).start();
    }
}
