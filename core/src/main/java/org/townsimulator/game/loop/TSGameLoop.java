package org.townsimulator.game.loop;

import jecs.core.GameLogic;
import jecs.core.GameLoop;

import java.util.function.Consumer;

public class TSGameLoop extends GameLoop {

    protected final Consumer<GameLogic> runnableLoopLogic;
    protected final GameLogic gameLogic;

    protected TSGameLoop(Consumer<GameLogic> runnableLoopLogic, GameLogic gameLogic) {
        this.runnableLoopLogic = runnableLoopLogic;
        this.gameLogic = gameLogic;
    }

    @Override
    public void start() {
        runnableLoopLogic.accept(gameLogic);
    }

    public void step() {
        start();
    }
}
