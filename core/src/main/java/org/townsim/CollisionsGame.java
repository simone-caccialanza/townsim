package org.townsim;


import jecs.core.GameLogic;
import org.townsimulator.game.logic.TSGameLogic;
import org.townsimulator.game.loop.TSGameLoop;

public class CollisionsGame {

    public static class CollisionGameLogic extends TSGameLogic {
        protected CollisionGameLogic(Runnable runnableLogic) {
            super(runnableLogic);
        }
    }

    public static class CollisionGameLoop extends TSGameLoop {
        protected CollisionGameLoop(java.util.function.Consumer<GameLogic> runnableLoopLogic, GameLogic gameLogic) {
            super(runnableLoopLogic, gameLogic);
        }
    }

}
