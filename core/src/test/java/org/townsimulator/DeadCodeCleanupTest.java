package org.townsimulator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertFalse(Files.exists(Path.of("core/src/main/java/org/townsimulator/systems/TimeSystem.java")));
    }

    @Test
    void gameLogicStoreUsesRegisteredWorldSystems() throws Exception {
        String source = Files.readString(Path.of("core/src/main/java/org/townsimulator/game/logic/GameLogicStore.java"));

        assertTrue(source.contains("updateSystems"));
        assertFalse(source.contains("new MovementSystem()"));
    }
}
