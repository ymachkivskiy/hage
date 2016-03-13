package org.hage.platform;

import org.hage.platform.boot.NodeBootstrapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        new NodeBootstrapper(new AnnotationConfigApplicationContext(BootConfiguration.class), args).start();
    }

}
