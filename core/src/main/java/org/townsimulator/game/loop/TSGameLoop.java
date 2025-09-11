package org.townsimulator.game.loop;

import jecs.core.GameLogic;
import jecs.core.GameLoop;

import java.util.function.Consumer;

public class TSGameLoop extends GameLoop {
    protected TSGameLoop(Consumer<GameLogic> runnableLoopLogic, GameLogic gameLogic) {
        super(runnableLoopLogic, gameLogic);
    }
}
