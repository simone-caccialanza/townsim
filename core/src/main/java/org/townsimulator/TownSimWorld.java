package org.townsimulator;

import jecs.core.World;
import jecs.core.clocks.GameClock;
import jecs.core.config.JecsConfig;
import org.townsimulator.components.Age;
import org.townsimulator.components.Building;
import org.townsimulator.components.FoodProvider;
import org.townsimulator.components.Happiness;
import org.townsimulator.components.Health;
import org.townsimulator.components.Hunger;
import org.townsimulator.components.Job;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Name;
import org.townsimulator.components.Position;
import org.townsimulator.components.Relationships;
import org.townsimulator.components.SeekingFood;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.components.TSSprite;
import org.townsimulator.components.Task;
import org.townsimulator.components.Thirst;
import org.townsimulator.components.manager.PlayerSpriteAtlas;
import org.townsimulator.systems.FoodProvidingSystem;
import org.townsimulator.systems.HungerSystem;
import org.townsimulator.systems.MovementSystem;
import org.townsimulator.systems.TaskSystem;

/**
 * Application-scoped holder for the ECS world and simulation clock.
 */
public final class TownSimWorld {

    private static World world;
    private static GameClock clock;
    private static volatile boolean running = true;
    private static int playerEntityId = -1;
    private static int maxSimulationTicks = 1000;

    private TownSimWorld() {
    }

    public static World create() {
        world = World.builder()
                .config(JecsConfig.load())
                .registerComponents(
                        Age.Component.class,
                        Building.Component.class,
                        FoodProvider.Component.class,
                        Happiness.Component.class,
                        Health.Component.class,
                        Hunger.Component.class,
                        Job.Component.class,
                        Movement.Component.class,
                        Name.Component.class,
                        Position.Component.class,
                        Relationships.Component.class,
                        SeekingFood.Component.class,
                        SpriteASCII.Component.class,
                        Task.Component.class,
                        Thirst.Component.class,
                        TSSprite.Component.class
                )
                .addSystem(new MovementSystem())
                .addSystem(new HungerSystem())
                .addSystem(new TaskSystem())
                .addSystem(new FoodProvidingSystem())
                .build();
        clock = new GameClock(world.config());
        return world;
    }

    public static World get() {
        if (world == null) {
            throw new IllegalStateException("TownSimWorld has not been initialized");
        }
        return world;
    }

    public static GameClock clock() {
        if (clock == null) {
            throw new IllegalStateException("TownSimWorld has not been initialized");
        }
        return clock;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void stop() {
        running = false;
    }

    public static void resetForTests() {
        world = null;
        clock = null;
        running = true;
        playerEntityId = -1;
        maxSimulationTicks = 1000;
        GlobalGrid.resetForTests();
        PlayerSpriteAtlas.resetForTests();
    }

    public static int maxSimulationTicks() {
        return maxSimulationTicks;
    }

    public static void setMaxSimulationTicks(int ticks) {
        maxSimulationTicks = ticks;
    }

    public static int playerEntityId() {
        return playerEntityId;
    }

    public static void setPlayerEntityId(int entityId) {
        playerEntityId = entityId;
    }
}
