package org.jage.platform.argument;


public interface RuntimeArgumentsService {

    void parse(String bootClassName, String arguments[]);

    String getPlatformOption(String key);

    boolean containsPlatformOption(String key);

    String getCustomOption(String key);

    boolean containsCustomOption(String key);

    void printUsage();

    boolean hasHelpOption();
}
