package org.townsimulator.game.loop;

import jecs.core.GameLogic;
import org.townsimulator.TownSimWorld;

import java.util.function.Consumer;

public class GameLoopStore {

    public static void stepFrame(GameLogic gl) {
        var clock = TownSimWorld.clock();
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

    public static final Consumer<GameLogic> SINGLE_FRAME_LOOP = (gl) -> {
        if (!TownSimWorld.isRunning()) {
            return;
        }
        if (TownSimWorld.clock().simulationTicks() >= TownSimWorld.maxSimulationTicks()) {
            return;
        }
        stepFrame(gl);
    };

    public static final Consumer<GameLogic> BASE_LOOP = (gl) -> {
        while (TownSimWorld.isRunning()
                && TownSimWorld.clock().simulationTicks() < TownSimWorld.maxSimulationTicks()) {
            stepFrame(gl);
        }
    };

    private GameLoopStore() {
    }
}
