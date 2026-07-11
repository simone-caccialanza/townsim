package org.townsim;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetPathTest {

    @Test
    void mapAssetUsesRelativeLibGdxPath() throws Exception {
        String source = Files.readString(sourcePath("org/townsim/Main.java"));

        assertTrue(source.contains("\"maps/map1.tmx\""));
        assertFalse(source.contains("C:\\\\Users"));
    }

    private static Path sourcePath(String relativePath) {
        Path fromCoreModule = Path.of("src/main/java", relativePath);
        if (Files.exists(fromCoreModule)) {
            return fromCoreModule;
        }
        return Path.of("core/src/main/java", relativePath);
    }
}
