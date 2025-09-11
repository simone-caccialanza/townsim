package org.townsim;


import jecs.core.GameLogic;
import jecs.core.clocks.GameClock;
import jecs.core.utils.GlobalConstants;
import jecs.core.utils.GlobalMutable;
import org.townsimulator.game.logic.TSGameLogic;
import org.townsimulator.game.loop.TSGameLoop;

import java.util.function.Consumer;

public class CollisionsGame {
    public static final Consumer<GameLogic> BASE_LOOP_STEP = (gl) -> {
        if (GlobalMutable.running && GameClock.ticks <= 100L) {
            GameClock.update();
            long startLoopTime = System.nanoTime();

            if (GameClock.shouldTick()) {
                gl.update();
                GameClock.tick();
            }

            long elapsedTime = System.nanoTime() - startLoopTime;
            long sleepTime = (long) (1000000000 / GlobalConstants.FPS) - elapsedTime;
            if (sleepTime > 0L) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                } catch (InterruptedException var8) {
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

        public void step() {
            runnableLoopLogic.accept(gameLogic);
        }
    }

}
