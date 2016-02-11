package org.hage.platform.config.load.xml;


public enum ConfigNamespaces {
    DEFAULT("age", "http://age.iisg.agh.edu.pl/schema/age");

    private final String prefix;

    private final String uri;

    ConfigNamespaces(final String prefix, final String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }


    public String getPrefix() {
        return prefix;
    }

    public String getUri() {
        return uri;
    }
}
