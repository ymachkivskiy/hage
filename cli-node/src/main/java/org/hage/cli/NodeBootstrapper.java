package org.hage.cli;


import org.hage.platform.PlatformCliCfg;
import org.hage.platform.PlatformCoreCfg;
import org.hage.platform.config.CommandLineArgumentsParser;
import org.hage.platform.util.boot.PlatformStarter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class NodeBootstrapper {

    private static final String HEADER =
              "+--------------------------------------------------------------------+\n"
            + "|Hh  hH   AAA           EEEEEEE    Nn    nN    OOOO   DDDDD   EEEEEEE|\n"
            + "|Hh  hH  AAAAA   gggggg EE         Nnn   nN   OooooO  DddddD  EE     |\n"
            + "|hhhhhH AA   AA gg   gg EEEEE      Nnnn  nN   Oo  oO  Dd   D  EEEEE  |\n"
            + "|hhhhhH AAAAAAA ggggggg EE         Nn nn nN   Oo  oO  Dd   D  EE     |\n"
            + "|Hh  hH AA   AA      gg EEEEEEE    Nn  nnnN   OooooO  DddddD  EEEEEEE|\n"
            + "|Hh  hH          ggggg             Nn    nN    OOOO   DDDDD          |\n"
            + "+--------------------------------------------------------------------+\n\n";

    private final AnnotationConfigApplicationContext parentCtxt;
    private final AnnotationConfigApplicationContext corePlatformCtxt;


    public NodeBootstrapper() {
        parentCtxt = new AnnotationConfigApplicationContext(PlatformCliCfg.class);
        corePlatformCtxt = new AnnotationConfigApplicationContext();

        corePlatformCtxt.register(PlatformCoreCfg.class);
        corePlatformCtxt.setParent(parentCtxt);
    }

    public void start(String[] args) {
        parseArguments(args);
        System.out.println(HEADER);
        startPlatform();
    }

    private void parseArguments(String[] args) {
        CommandLineArgumentsParser argumentsParser = parentCtxt.getBean(CommandLineArgumentsParser.class);
        argumentsParser.parse(args);
    }

    private void startPlatform() {
        corePlatformCtxt.refresh();
        PlatformStarter platformStarter = corePlatformCtxt.getBean(PlatformStarter.class);
        platformStarter.start();
    }

    public static void main(String[] args) {
        new NodeBootstrapper().start(args);
    }

}
