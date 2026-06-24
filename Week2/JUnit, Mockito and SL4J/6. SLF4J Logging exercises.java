import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;

/**
 * SLF4J / Logback solutions in plain Java.
 */
class Slf4jLoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jLoggingExample.class);

    public static void main(String[] args) {
        logger.error("This is an error message");
        logger.warn("This is a warning message");
    }
}

// Exercise 2: Parameterized Logging
class Slf4jParameterizedLoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jParameterizedLoggingExample.class);

    public static void main(String[] args) {
        String user = "Pratibimb";
        int attempts = 3;
        logger.info("Login failed for user {} after {} attempts", user, attempts);
        logger.debug("Debugging payload id={} status={}", 42, "OK");
    }
}

// Exercise 3: Different Appenders (programmatic Logback configuration)
class Slf4jAppenderDemo {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jAppenderDemo.class);

    public static void main(String[] args) {
        configureLogging();
        logger.info("Console and file appenders are active.");
        logger.warn("This warning will go to both destinations.");
    }

    private static void configureLogging() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setName("console");
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(context);
        fileAppender.setName("file");
        fileAppender.setFile("app.log");
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        ch.qos.logback.classic.Logger root = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);
        root.addAppender(consoleAppender);
        root.addAppender(fileAppender);
    }
}
