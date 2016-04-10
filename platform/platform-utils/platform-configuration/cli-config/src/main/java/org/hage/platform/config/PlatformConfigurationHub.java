package org.hage.platform.config;

import org.hage.platform.config.parse.ParseResult;
import org.hage.platform.config.parse.ParsingEngine;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.hage.util.CollectionUtils.nullSafe;

public class PlatformConfigurationHub implements ConfigurationProvider, CommandLineArgumentsParser {

    @Autowired(required = false)
    private List<ConfigurationCategorySupplier> configurationCategorySuppliers;
    @Autowired
    private ParsingEngine parsingEngine;
    private ParseResult argumentValues;

    public PlatformConfigurationHub() {
        boolean dupa = false;
    }

    @Override
    public void parse(String[] arguments) throws InvalidRuntimeArgumentsException {
        argumentValues = parsingEngine.parse(arguments);
    }

    @Override
    public <T> Optional<T> getValueOf(ConfigurationItem item, Class<T> valueClass) {
        return ofNullable(argumentValues.getValues().get(item))
            .filter(valueClass::isInstance)
            .map(valueClass::cast);
    }

    @PostConstruct
    private void initEngine() {
        nullSafe(configurationCategorySuppliers).stream()
            .map(ConfigurationCategorySupplier::getConfigurationCategory)
            .sorted((cat1, cat2) -> cat1.getCategoryName().compareTo(cat2.getCategoryName()))
            .forEach(parsingEngine::addCategory);
    }

}
