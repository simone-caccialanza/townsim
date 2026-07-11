package org.townsimulator.game.logic;

import org.townsimulator.TownSimWorld;

public class GameLogicStore {

    public static final Runnable BASE_LOGIC_MOVEMENT_ONLY = () ->
            TownSimWorld.get().updateSystems(TownSimWorld.clock().timePerTick());

    public static final Runnable BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY = () ->
            TownSimWorld.get().updateSystems(TownSimWorld.clock().timePerTick());

    private GameLogicStore() {
    }
}
