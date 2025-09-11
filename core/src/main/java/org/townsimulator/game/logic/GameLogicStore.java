package org.townsimulator.game.logic;

import jecs.core.clocks.GameClock;
import org.townsimulator.systems.FoodProvidingSystem;
import org.townsimulator.systems.HungerSystem;
import org.townsimulator.systems.MovementSystem;
import org.townsimulator.systems.TaskSystem;
import org.townsimulator.utils.PrintUtils;

public class GameLogicStore {

    public static final Runnable BASE_LOGIC_MOVEMENT_ONLY = () -> {
        if (GameClock.ticks % 2 == 0) {
            MovementSystem.getInstance().run();
//            PrintUtils.printGridASCII();
        }
    };
    public static final Runnable BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY = () -> {
        MovementSystem.getInstance().run();
        HungerSystem.getInstance().run();
        TaskSystem.getInstance().run();
        FoodProvidingSystem.getInstance().run();
        //PrintUtils.printGridASCII();
    };

    private GameLogicStore() {
    }
}
