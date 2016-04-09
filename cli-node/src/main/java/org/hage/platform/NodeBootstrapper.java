package org.hage.platform;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.config.CommandLineArgumentsParser;
import org.hage.platform.util.boot.PlatformStarter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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
        System.out.println(HEADER);

        this.applicationContext = applicationContext;

        CommandLineArgumentsParser parser = this.applicationContext.getBean(CommandLineArgumentsParser.class);

        parser.parse(args);

    }


    public void start() {
        log.debug("Starting HAgE Node from the command line.");

        applicationContext.getBean(PlatformStarter.class).start();

        log.debug("Bootstrapping finished.");
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctxt = new AnnotationConfigApplicationContext(BootConfiguration.class);

        new NodeBootstrapper(ctxt, args).start();
    }


}
