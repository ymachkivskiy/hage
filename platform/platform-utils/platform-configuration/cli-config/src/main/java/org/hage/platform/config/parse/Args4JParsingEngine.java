package org.hage.platform.config.parse;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Args4JParsingEngine implements ParsingEngine {

    private final Map<String, ConfigurationItem> itemsMap = new HashMap<>();
    private final ArgumentParser parser;

    public Args4JParsingEngine() {
        parser = ArgumentParsers.newArgumentParser("hage").defaultHelp(true);
    }

    @Override
    public ParseResult parse(String[] args) {
        return getCheckedParseResult(parser.parseArgsOrFail(args));
    }

    @Override
    public void addCategory(ConfigurationCategory category) {
        ArgumentGroup group = parser.addArgumentGroup(category.getCategoryName());
        group.description(category.getCategoryDescription());

        for (ConfigurationItem item : category.getConfigItems()) {
            insertItem(group, item);
            itemsMap.put(item.getItemId(), item);
        }

    }

    private ParseResult getCheckedParseResult(Namespace namespace) {
        Map<ConfigurationItem, Object> results = new HashMap<>();

        for (Entry<String, Object> entry : namespace.getAttrs().entrySet()) {
            ConfigurationItem item = itemsMap.get(entry.getKey());
            item.checkValue(entry.getValue());
            results.put(item, entry.getValue());
        }

        return new ParseResult(results);
    }

    private void insertItem(ArgumentGroup group, ConfigurationItem item) {
        ConfigurationItemProperties p = item.getProperties();

        Argument arg = group.addArgument("-" + p.getShortName(), "--" + p.getLongName());

        arg.help(p.getDescription());
        if (p.isHasValue()) {
            arg.nargs(1).metavar(p.getValueName()).type(p.getValueType());

            if (p.getDefaultValue() != null) {
                arg.setDefault(p.getDefaultValue());
            }
        }

        arg.dest(item.getItemId());
    }


}
