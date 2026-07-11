package org.townsim;


import jecs.core.GameLogic;
import org.townsimulator.TownSimWorld;
import org.townsimulator.game.logic.TSGameLogic;
import org.townsimulator.game.loop.TSGameLoop;

import java.util.function.Consumer;

public class CollisionsGame {
    public static final Consumer<GameLogic> BASE_LOOP_STEP = (gl) -> {
        var clock = TownSimWorld.clock();
        if (TownSimWorld.isRunning() && clock.simulationTicks() <= 100L) {
            clock.advanceFrame();
            long startLoopTime = System.nanoTime();

            if (clock.hasPendingTick()) {
                gl.update();
                clock.consumeTick();
            }

            long elapsedTime = System.nanoTime() - startLoopTime;
            long sleepTime = TownSimWorld.get().config().targetFrameNanos() - elapsedTime;
            if (sleepTime > 0L) {
                try {
                    Thread.sleep(sleepTime / 1_000_000L);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    };


    public static class CollisionGameLogic extends TSGameLogic {
        protected CollisionGameLogic(Runnable runnableLogic) {
            super(runnableLogic);
        }
    }

    public static class CollisionGameLoop extends TSGameLoop {
        protected CollisionGameLoop(Consumer<GameLogic> runnableLoopLogic, GameLogic gameLogic) {
            super(runnableLoopLogic, gameLogic);
        }
    }

}
