package org.townsimulator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingTests {
    private static final Logger log = LoggerFactory.getLogger(LoggingTests.class);


    @Test
    void loggingTest() {
        log.info(System.getProperty("enableProfileLogger"));
        log.info(System.getProperty("consoleAppender"));

        log.debug("Debug message");
        log.info("Info message");
    }

}