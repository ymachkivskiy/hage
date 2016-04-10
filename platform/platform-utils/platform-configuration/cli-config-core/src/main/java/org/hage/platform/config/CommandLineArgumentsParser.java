package org.hage.platform.config;

public interface CommandLineArgumentsParser {
    void parse(String[] arguments) throws InvalidRuntimeArgumentsException;
}
