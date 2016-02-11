package org.hage.platform.config.load.xml;



public enum ConfigAttributes {
    NAME("name"),
    CLASS("class"),
    IS_SINGLETON("isSingleton"),
    TARGET("target"),
    VALUE("value"),
    TYPE("type"),
    KEY_TYPE("key-type"),
    VALUE_TYPE("value-type"),
    FILE("file"),
    COUNT("count"),
    OVERRIDE("override"),
    REF("ref"),
    KEY("key"),
    KEY_REF("key-ref"),
    VALUE_REF("value-ref");

    private final String value;

    ConfigAttributes(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
