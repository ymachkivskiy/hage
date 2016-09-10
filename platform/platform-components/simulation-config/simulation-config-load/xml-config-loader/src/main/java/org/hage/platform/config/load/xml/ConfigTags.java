package org.hage.platform.simulationconfig.load.xml;


public enum ConfigTags {

    CONFIGURATION("configuration"),
    COMPONENT("component"),
    AGENT("agent"),
    STRATEGY("strategy"),
    ARRAY("array"),
    LIST("list"),
    SET("set"),
    MAP("map"),
    CONSTRUCTOR_ARG("constructor-arg"),
    PROPERTY("property"),
    VALUE("value"),
    REFERENCE("reference"),
    ENTRY("entry"),
    KEY("key"),
    INCLUDE("include"),
    MULTIPLE("multiple"),
    BLOCK("block");

    private final String value;

    ConfigTags(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
