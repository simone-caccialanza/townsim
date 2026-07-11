package org.townsim;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetPathTest {

    @Test
    void mapAssetUsesRelativeLibGdxPath() throws Exception {
        String source = Files.readString(Path.of("core/src/main/java/org/townsim/Main.java"));

        assertTrue(source.contains("\"maps/map1.tmx\""));
        assertTrue(!source.contains("C:\\\\Users"));
    }
}
