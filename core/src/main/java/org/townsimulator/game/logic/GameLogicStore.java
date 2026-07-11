package org.townsimulator.game.logic;

import org.townsimulator.TownSimWorld;
import org.townsimulator.systems.MovementSystem;

public class GameLogicStore {

    public static final Runnable BASE_LOGIC_MOVEMENT_ONLY = () -> {
        if (TownSimWorld.clock().simulationTicks() % 2 == 0) {
            new MovementSystem().run(TownSimWorld.get(), TownSimWorld.clock().timePerTick());
        }
    };

    public static final Runnable BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY = () ->
            TownSimWorld.get().updateSystems(TownSimWorld.clock().timePerTick());

    private GameLogicStore() {
    }
}
