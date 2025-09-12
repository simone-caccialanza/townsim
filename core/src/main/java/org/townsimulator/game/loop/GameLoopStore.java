package org.townsimulator.game.loop;

import jecs.core.GameLogic;
import jecs.core.clocks.GameClock;

import java.util.function.Consumer;

import static jecs.core.utils.GlobalConstants.TICK_PER_SECOND;
import static jecs.core.utils.GlobalMutable.running;

public class GameLoopStore {

    public static final Consumer<GameLogic> BASE_LOOP = (gl) -> {
        while (running) {
            if (GameClock.ticks > 1000) break;

            GameClock.update();
            long startLoopTime = System.nanoTime();
            while (GameClock.shouldTick()) {
                gl.update();
                GameClock.tick();
            }

            long elapsedTime = System.nanoTime() - startLoopTime; // Time taken by the loop
            long sleepTime = 1_000_000_000 / TICK_PER_SECOND - elapsedTime; // Nanosecond per frame time

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000); // Sleep if there's still time left in the frame
                } catch (InterruptedException e) {
                }
            }
        }
    };

    private GameLoopStore() {
    }
}
