package org.townsimulator;

import jecs.core.GameLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.game.loop.GameLoopStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY;

class GameLoopStoreTest {

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        TownSimWorld.create();
        TownSimWorld.setMaxSimulationTicks(3);
    }

    @Test
    void singleFrameLoopAdvancesAtMostOneFramePerCall() {
        GameLogic gameLogic = new GameLogic(BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY) {};
        long ticksBefore = TownSimWorld.clock().simulationTicks();

        GameLoopStore.SINGLE_FRAME_LOOP.accept(gameLogic);

        assertTrue(TownSimWorld.clock().simulationTicks() >= ticksBefore);
    }

    @Test
    void baseLoopStopsAtConfiguredMaxTicks() {
        GameLogic gameLogic = new GameLogic(BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY) {};

        GameLoopStore.BASE_LOOP.accept(gameLogic);

        assertEquals(3, TownSimWorld.clock().simulationTicks());
    }
}
