package org.hage.platform.rate.local.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Settings {

    private int b;

    @Autowired
    private Environment env;

    @PostConstruct
    private void test() {
        String prop = env.getProperty("hage.platform.rate.minimalRate");
        boolean d = false;
    }

}
