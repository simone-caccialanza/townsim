package org.townsimulator;

import jecs.core.Game;
import jecs.core.GameLogic;
import jecs.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.FoodProvider;
import org.townsimulator.components.Hunger;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Position;
import org.townsimulator.components.SeekingFood;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.components.Task;
import org.townsimulator.game.loader.TSGameLoader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY;
import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_ONLY;
import static org.townsimulator.game.loop.GameLoopStore.BASE_LOOP;

class SimulationIntegrationTest {

    private World world;

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        TownSimWorld.setMaxSimulationTicks(10);
        world = TownSimWorld.create();
    }

    @Test
    void movementSystemRunsWithoutError() {
        world.createEntity(
                new Movement.Component(1, 2, 7, 8),
                new Position.Component(0, 0),
                new Hunger.Component(1, 9.97f),
                new SpriteASCII.Component('E')
        );
        world.createEntity(
                new Position.Component(7, 8),
                new SpriteASCII.Component('B')
        );

        runSimulation(BASE_LOGIC_MOVEMENT_ONLY);

        assertTrue(TownSimWorld.clock().simulationTicks() > 0);
    }

    @Test
    void hungryEntityReceivesGoToTask() {
        var entity = world.createEntity(
                new Movement.Component(0, 0),
                new Position.Component(0, 0),
                new Hunger.Component(1, 9.97f),
                new SpriteASCII.Component('E'),
                new Task.Component()
        );
        world.createEntity(
                new Position.Component(7, 8),
                new SpriteASCII.Component('B'),
                new FoodProvider.Component(3)
        );

        var hunger = world.getComponent(entity.id(), Hunger.Component.class);
        hunger.hunger = 11f;
        hunger.isHungry = true;

        runSimulation(BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY);

        var task = world.getComponent(entity.id(), Task.Component.class);
        assertNotNull(task);
        assertFalse(task.actionQueue.isEmpty());
        assertTrue(world.hasComponent(entity.id(), SeekingFood.Component.class));
    }

    @Test
    void collisionScenarioRunsWithoutError() {
        world.createEntity(
                new Movement.Component(1, 1, 7, 1),
                new Position.Component(0, 1, true),
                new SpriteASCII.Component('A')
        );
        world.createEntity(
                new Movement.Component(1, 1, 1, 1),
                new Position.Component(8, 1, true),
                new SpriteASCII.Component('B')
        );

        GlobalGrid.getInstance().bindSprites(world);
        GlobalGrid.getInstance().cellAt(2, 2).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(2, 3).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(3, 3).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(4, 3).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(5, 3).movementWeight = 1;

        runSimulation(BASE_LOGIC_MOVEMENT_ONLY);

        assertTrue(TownSimWorld.clock().simulationTicks() > 0);
    }

    private void runSimulation(Runnable logic) {
        GlobalGrid.getInstance().bindSprites(world);
        GameLogic gameLogic = new GameLogic(logic) {};
        var gameLoop = new TestGameLoop(BASE_LOOP, gameLogic);
        new Game(new TSGameLoader(), gameLoop).start();
    }

    private static final class TestGameLoop extends org.townsimulator.game.loop.TSGameLoop {
        TestGameLoop(java.util.function.Consumer<GameLogic> runnableLoopLogic, GameLogic gameLogic) {
            super(runnableLoopLogic, gameLogic);
        }
    }
}
