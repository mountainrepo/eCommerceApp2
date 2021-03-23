package com.udacity.course4.config;

import org.apache.commons.lang3.SystemUtils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.net.URI;

public class CustomLoggingConfiguration extends ConfigurationFactory {

    @Override
    public Configuration getConfiguration(final LoggerContext context, final ConfigurationSource source) {
        context.setName("auth-course-logging-context");
        System.out.println("First Override " + context.getName() + " end");

        return getConfiguration(context, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext context, final String source, final URI configLocation) {
        System.out.println("Second Override");

        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();

        return createConfiguration(source, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }

    static Configuration createConfiguration(final String source, ConfigurationBuilder<BuiltConfiguration> builder) {
        System.out.println("Configuring");

        builder.setConfigurationName(source);
        // Log file path and Archive file path
        //String filePath = new File("").getAbsolutePath();
        String logFilePath = null;
        String archiveFilePath = null;

        //System.out.println("Current file path is " + filePath);

        /*
        if(SystemUtils.IS_OS_WINDOWS) {
            logFilePath = filePath + "\\log\\logging.log";
            archiveFilePath = filePath + "\\archive\\";
        }

        if(SystemUtils.IS_OS_LINUX) {
            logFilePath = filePath + "/log/logging.log";
            archiveFilePath = filePath + "/archive";
        }
         */

        if(SystemUtils.IS_OS_WINDOWS) {
            logFilePath = "C:\\auth_course_log\\log\\logging.log";
            archiveFilePath = "C:\\auth_course_log\\archive\\";
        }

        if(SystemUtils.IS_OS_LINUX) {
            logFilePath = "/var/auth_course_log/log/logging.log";
            archiveFilePath = "var/auth_course_log/archive";
        }

        // Create Layout for events
        // <Timestamp> [<Thread_Name>] <Logging_Level>: <Logging_Message><Line_Separator>
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                                                      .addAttribute("pattern", "%d [%t] %level: %msg%n");

        // Create Console appender
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE")
                                                          .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                                                          .add(layoutBuilder);

        builder.add(appenderBuilder);

        // Create RollingFile appender
        // Create Triggering Policies
        ComponentBuilder triggeringPolicies = builder.newComponent("Policies")
                                                   .addComponent(builder.newComponent("TimeBasedTriggeringPolicy"))
                                                   .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "10M"));

        // Rolling file name, archive path and pattern, layout, triggering policy, rolling strategy
        appenderBuilder = builder.newAppender("rolling", "RollingFile")
                                 .addAttribute("fileName", logFilePath)
                                 .addAttribute("filePattern", archiveFilePath + "rolling-%d{MM-dd-YYYY}.log.gz")
                                 .add(layoutBuilder)
                                 .addComponent(triggeringPolicies);

        builder.add(appenderBuilder);

        // Create Logger
        builder.add(builder.newRootLogger( Level.INFO )
                           .add(builder.newAppenderRef("Stdout")));

        builder.add(builder.newLogger("com.udacity.course4", Level.INFO)
                           .add(builder.newAppenderRef("rolling"))
                           .addAttribute("additivity", false));

        System.out.println("Current file path is " + logFilePath);

        // Build configuration
        return builder.build();
    }
}
