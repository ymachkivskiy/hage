package org.hage.platform;


import org.hage.CmdConfiguration;
import org.hage.platform.config.CommandLineArgumentsParser;
import org.hage.platform.util.boot.PlatformStarter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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


    public static void main(String[] args) {

        ApplicationContext parsingCtxt = parseArguments(args);

        System.out.println(HEADER);

        getPlatformStarter(parsingCtxt).start();

    }

    private static PlatformStarter getPlatformStarter(ApplicationContext parsingCtxt) {
        AnnotationConfigApplicationContext ctxt = new AnnotationConfigApplicationContext();

        ctxt.register(BootConfiguration.class);
        ctxt.setParent(parsingCtxt);
        ctxt.refresh();

        return ctxt.getBean(PlatformStarter.class);
    }

    private static ApplicationContext parseArguments(String[] args) {
        AnnotationConfigApplicationContext parsingCtxt = new AnnotationConfigApplicationContext(CmdConfiguration.class);
        parsingCtxt.getBean(CommandLineArgumentsParser.class).parse(args);

        return parsingCtxt;
    }


}
