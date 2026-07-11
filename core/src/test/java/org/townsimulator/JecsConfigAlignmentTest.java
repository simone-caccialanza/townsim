package org.townsimulator;

import jecs.core.config.JecsConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JecsConfigAlignmentTest {

    @Test
    void testPropertiesUseTickPerSecondKey() {
        assertEquals(2, JecsConfig.load().tickPerSecond());
    }

    @Test
    void testPropertiesMatchMainEntityCapacity() {
        assertEquals(100, JecsConfig.load().maxEntityNumber());
    }
}
