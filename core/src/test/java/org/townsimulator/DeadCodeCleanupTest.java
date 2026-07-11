package org.townsimulator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.game.logic.GameLogicStore;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeadCodeCleanupTest {

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        TownSimWorld.create();
    }

    @Test
    void timeSystemSourceWasRemoved() {
        assertFalse(Files.exists(sourcePath("org/townsimulator/systems/TimeSystem.java")));
    }

    @Test
    void gameLogicStoreUsesRegisteredWorldSystems() throws Exception {
        String source = Files.readString(sourcePath("org/townsimulator/game/logic/GameLogicStore.java"));

        assertTrue(source.contains("updateSystems"));
        assertFalse(source.contains("new MovementSystem()"));
        assertTrue(GameLogicStore.BASE_LOGIC_MOVEMENT_ONLY != null);
    }

    private static Path sourcePath(String relativePath) {
        Path fromCoreModule = Path.of("src/main/java", relativePath);
        if (Files.exists(fromCoreModule)) {
            return fromCoreModule;
        }
        return Path.of("core/src/main/java", relativePath);
    }
}
