package org.townsimulator.game.loop;

import jecs.core.GameLogic;
import org.townsimulator.TownSimWorld;

import java.util.function.Consumer;

public class GameLoopStore {

    public static final Consumer<GameLogic> BASE_LOOP = (gl) -> {
        var clock = TownSimWorld.clock();
        while (TownSimWorld.isRunning()) {
            if (clock.simulationTicks() > TownSimWorld.maxSimulationTicks()) {
                break;
            }

            clock.advanceFrame();
            long startLoopTime = System.nanoTime();
            while (clock.hasPendingTick()) {
                gl.update();
                clock.consumeTick();
            }

            long elapsedTime = System.nanoTime() - startLoopTime;
            long sleepTime = TownSimWorld.get().config().targetFrameNanos() - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000L);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    };

    private GameLoopStore() {
    }
}
