package org.townsimulator;

import jecs.core.config.JecsConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JecsConfigTest {

    @Test
    void loadsTickRateFromProperties() {
        assertEquals(2, JecsConfig.load().tickPerSecond());
    }

    @Test
    void exposesPositiveEntityCapacity() {
        assertTrue(JecsConfig.load().maxEntityNumber() > 0);
    }
}
