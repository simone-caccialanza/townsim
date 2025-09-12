package org.townsimulator;

import jecs.core.utils.GlobalConstants;
import org.annotationlib.AnnotationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.townsimulator.game.loader.TSGameLoader;

import java.util.List;

class GameLoaderTest {

    @Test
    void load() {
        var gameLoader = new TSGameLoader();
        gameLoader.run();
    }

    @Test
    void globalConstants() {
        Assertions.assertEquals(2, GlobalConstants.TICK_PER_SECOND);
    }

    @Test
    void getClassesInPackages() {
        var clz = AnnotationUtils.getClassesInPackages(List.of("org.townsimulator.components",
                "org.annotationlib.annotations", "org.townsimulator.systems", "org.ECSlib", "org.townsimulator",
                "org.townsimulator.utils", "org.ECSlib.clocks", "org.annotationlib", "org.ECSlib.system", "org.ECSlib.utils"));
        Assertions.assertFalse(clz.isEmpty());
    }
}
