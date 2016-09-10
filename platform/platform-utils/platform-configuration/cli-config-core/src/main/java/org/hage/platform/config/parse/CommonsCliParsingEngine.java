package org.hage.platform.config.parse;




import org.apache.commons.cli.*;
import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;

import java.io.PrintWriter;
import java.io.StringWriter;


public class CommonsCliParsingEngine implements ParsingEngine {

    private final Options options;
    private CommandLine commandLine;

    public CommonsCliParsingEngine() {
        options = new Options();
        Option helpOpt = Option.builder("h").longOpt("help").desc("Prints help for application.").build();
        options.addOption(helpOpt);
    }

    @Override
    public ParseResult parse(String[] arguments) {
        try {
            commandLine = new DefaultParser().parse(options, arguments);
            return null;
            // TODO: implement
        } catch (ParseException e) {
            printUsage();
            System.exit(0);
        }
        return null;
    }

    @Override
    public void addCategory(ConfigurationCategory category) {
        for (ConfigurationItem item : category.getConfigItems()) {
            ConfigurationItemProperties p = item.getProperties();

            Option.Builder builder = Option.builder();
            if (p.getShortName() != null) {
                Option.builder(p.getShortName());
            }
            if (p.getLongName() != null) {
                builder.longOpt(p.getLongName());
            }

            builder.desc(p.getDescription());

            if (p.isHasValue()) {
                builder
                    .hasArg()
                    .type(p.getValueType())
                    .argName(p.getValueName());
            }

            builder.required(p.isRequired());

            options.addOption(builder.build());
        }
    }

    private void printUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        formatter.printUsage(printWriter, 120, "hage", options);
        System.out.println(stringWriter.toString());
    }


}
